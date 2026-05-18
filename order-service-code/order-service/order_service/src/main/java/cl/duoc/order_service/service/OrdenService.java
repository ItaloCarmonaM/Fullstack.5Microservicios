package cl.duoc.order_service.service;

import cl.duoc.order_service.client.CarritoClient;
import cl.duoc.order_service.client.UserClient;
import cl.duoc.order_service.dto.CarritoDTO;
import cl.duoc.order_service.dto.OrdenDTO;
import cl.duoc.order_service.dto.UserDTO;
import cl.duoc.order_service.exception.RecursoNoEncontradoException;
import cl.duoc.order_service.exception.ServicioNoDisponibleException;
import cl.duoc.order_service.model.Orden;
import cl.duoc.order_service.repository.OrdenRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenService {

    private static final Logger log = LoggerFactory.getLogger(OrdenService.class);

    private final OrdenRepository ordenRepository;

    @Autowired
    private CarritoClient carritoClient;

    @Autowired
    private UserClient userClient;

    public OrdenService(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

@Transactional
    public OrdenDTO crearOrden(cl.duoc.order_service.dto.OrdenCreateDTO dto) {
        // Extraemos el idUsuario que viene dentro del JSON del Body
        Long idUsuario = dto.getIdUsuario();

        log.info("Iniciando creación de orden para el usuario ID={} (Recibido desde RequestBody)", idUsuario);
        log.info("Solicitando validación de existencia para usuario ID={} al servicio remoto de Usuarios", idUsuario);
        try {
            UserDTO usuario = userClient.getUserById(idUsuario);
            log.info("Usuario verificado con éxito en la otra EC2. Comprador: '{}'", usuario.getNombreCompleto());
        } catch (FeignException.NotFound e) {
            log.warn("Servicio de Usuarios respondió 404: El usuario ID={} no existe", idUsuario);
            throw new RecursoNoEncontradoException("No se puede procesar la orden: El usuario especificado no existe.");
        } catch (FeignException e) {
            log.error("Error crítico de comunicación con la EC2 de Usuarios: {}", e.getMessage());
            throw new ServicioNoDisponibleException("El servicio de verificación de usuarios no está disponible.");
        }

        log.info("Solicitando ítems del carrito con montos precalculados al servicio Cart para usuario ID={}", idUsuario);
        List<CarritoDTO> items;
        try {
            items = carritoClient.obtenerCarrito(idUsuario);
        } catch (FeignException.NotFound e) {
            log.warn("Servicio Cart respondió 404: No se encontró el carrito para el usuario ID={}", idUsuario);
            throw new RecursoNoEncontradoException("No se puede procesar la orden: El carrito del usuario no existe.");
        } catch (FeignException e) {
            log.error("Falla de comunicación con el servicio Cart: {}", e.getMessage());
            throw new ServicioNoDisponibleException("El servicio de Carrito no se encuentra disponible.");
        }

        if (items == null || items.isEmpty()) {
            log.warn("Intento de compra rechazado: El carrito del usuario ID={} está vacío", idUsuario);
            throw new RuntimeException("El carrito está vacío.");
        }

        log.info("Calculando total acumulado usando subtotales del carrito...");
        double totalOrden = items.stream()
                                 .mapToDouble(item -> item.getSubtotal() != null ? item.getSubtotal() : 0.0)
                                 .sum();

        log.info("Monto total verificado dinámicamente mediante el carrito: ${} (Se descarta el total enviado por Postman por seguridad)", totalOrden);

        Orden nuevaOrden = new Orden();
        nuevaOrden.setIdUsuario(idUsuario);
        nuevaOrden.setTotal(totalOrden); // Se sigue calculando de forma segura en el backend
        nuevaOrden.setFechaCreacion(LocalDateTime.now()); // Auto-asignado
        nuevaOrden.setEstado("PROCESADA");
        
        Orden ordenGuardada = ordenRepository.save(nuevaOrden);
        log.info("Orden guardada exitosamente con ID={} en estado PROCESADA", ordenGuardada.getId());

        log.info("Solicitando vaciado síncrono del carrito para el usuario ID={}", idUsuario);
        try {
            carritoClient.vaciar(idUsuario);
            log.info("El servicio Cart limpió el carrito del usuario ID={} correctamente", idUsuario);
        } catch (Exception e) {
            log.error("Alerta de consistencia: No se pudo vaciar el carrito del usuario ID={} tras procesar la orden: {}", 
                      idUsuario, e.getMessage());
        }

        return convertirADTO(ordenGuardada);
    }

    public List<OrdenDTO> listarTodas() {
        log.info("Solicitando listado completo de todas las órdenes");
        return ordenRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public OrdenDTO buscarPorId(Long id) {
        log.info("Buscando orden por ID={}", id);
        return ordenRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> {
                    log.warn("Intento fallido de buscar orden: ID={} no existe", id);
                    return new RecursoNoEncontradoException("Orden no encontrada: " + id);
                });
    }

    public List<OrdenDTO> buscarPorUsuario(Long idUsuario) {
        log.info("Buscando órdenes del usuario ID={}", idUsuario);

        try {
            userClient.getUserById(idUsuario);
        } catch (FeignException.NotFound e) {
            log.warn("Intento de listar órdenes para un usuario que no existe en el sistema: ID={}", idUsuario);
            throw new RecursoNoEncontradoException("El usuario especificado no existe.");
        } catch (FeignException e) {
            log.error("No se pudo conectar con Usuarios para validar el listado de órdenes; continuando por tolerancia a fallos.");
        }

        return ordenRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public OrdenDTO actualizarEstado(Long id, String nuevoEstado) {
        log.info("Solicitud para cambiar estado de la orden ID={} a '{}'", id, nuevoEstado);
        return ordenRepository.findById(id).map(o -> {
            o.setEstado(nuevoEstado.toUpperCase());
            Orden actualizada = ordenRepository.save(o);
            log.info("Estado de orden ID={} cambiado con éxito", id);
            return convertirADTO(actualizada);
        }).orElseThrow(() -> {
            log.warn("No se pudo actualizar estado: Orden ID={} no existe", id);
            return new RecursoNoEncontradoException("Orden no encontrada: " + id);
        });
    }

    public boolean eliminarOrden(Long id) {
        log.info("Intentando eliminar orden ID={}", id);
        if (ordenRepository.existsById(id)) {
            ordenRepository.deleteById(id);
            log.info("Orden ID={} eliminada con éxito", id);
            return true;
        }
        log.warn("No se pudo eliminar: Orden ID={} no existe", id);
        return false;
    }

    private OrdenDTO convertirADTO(Orden o) {
        return new OrdenDTO(o.getId(), o.getIdUsuario(), o.getFechaCreacion(), o.getTotal(), o.getEstado());
    }
}
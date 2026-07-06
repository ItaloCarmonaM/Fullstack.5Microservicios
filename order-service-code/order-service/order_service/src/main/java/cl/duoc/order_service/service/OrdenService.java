package cl.duoc.order_service.service;

import cl.duoc.order_service.client.CarritoClient;
import cl.duoc.order_service.client.UserClient;
import cl.duoc.order_service.dto.CarritoDTO;
import cl.duoc.order_service.dto.OrdenDTO;
import cl.duoc.order_service.dto.UserDTO;
import cl.duoc.order_service.dto.OrdenCreateDTO;
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
    public OrdenDTO crearOrden(OrdenCreateDTO dto) {
        Long idUsuario = dto.getIdUsuario();

        log.info("Iniciando flujo de creación de orden para el usuario ID={}", idUsuario);
        log.info("Solicitando validación de existencia para usuario ID={} al servicio remoto de Usuarios", idUsuario);
        
        try {
            UserDTO usuario = userClient.getUserById(idUsuario);
            log.info("Usuario verificado con éxito. Comprador registrado: '{}'", usuario.getNombreCompleto());
        } catch (FeignException.NotFound e) {
            log.warn("Servicio de Usuarios respondió con error 404: El usuario ID={} no existe", idUsuario);
            throw new RecursoNoEncontradoException("No se puede procesar la orden: El usuario especificado no existe.");
        } catch (FeignException e) {
            log.error("Error de comunicación de red con el microservicio de Usuarios: {}", e.getMessage());
            throw new ServicioNoDisponibleException("El servicio de verificación de usuarios no está disponible temporeramente.");
        }

        log.info("Solicitando ítems del carrito al servicio Cart para usuario ID={}", idUsuario);
        List<CarritoDTO> items;
        try {
            items = carritoClient.obtenerCarrito(idUsuario);
            log.info("Respuesta exitosa recibida del servicio Cart para usuario ID={}", idUsuario);
        } catch (FeignException.NotFound e) {
            log.warn("Servicio Cart respondió con error 404 para el usuario ID={}", idUsuario);
            throw new RecursoNoEncontradoException("No se puede procesar la orden: El carrito del usuario no existe.");
        } catch (FeignException e) {
            log.error("Falla crítica de comunicación con el servicio de Carritos: {}", e.getMessage());
            throw new ServicioNoDisponibleException("El servicio de Carrito no se encuentra disponible.");
        }

        if (items == null || items.isEmpty()) {
            log.warn("Operación rechazada: El carrito del usuario ID={} no contiene productos", idUsuario);
            throw new RuntimeException("El carrito está vacío.");
        }

        log.info("Calculando total acumulado usando subtotales del carrito...");
        double totalOrden = items.stream()
                .mapToDouble(item -> item.getSubtotal() != null ? item.getSubtotal() : 0.0)
                .sum();

        log.info("Monto total verificado dinámicamente: ${}", totalOrden);

        Orden nuevaOrden = new Orden();
        nuevaOrden.setIdUsuario(idUsuario);
        nuevaOrden.setTotal(totalOrden);
        nuevaOrden.setFechaCreacion(LocalDateTime.now());
        nuevaOrden.setEstado("PROCESADA");
        
        Orden ordenGuardada = ordenRepository.save(nuevaOrden);
        log.info("Evento de negocio: Orden guardada de forma local con ID={} en estado PROCESADA", ordenGuardada.getId());

        log.info("Enviando petición síncrona de vaciado al servicio Cart para el usuario ID={}", idUsuario);
        try {
            carritoClient.vaciar(idUsuario);
            log.info("El servicio Cart limpió de forma exitosa el carrito del usuario ID={}", idUsuario);
        } catch (FeignException e) {
            log.error("Alerta de consistencia: No se pudo vaciar el carrito del usuario ID={} tras procesar la compra. Detalles: {}", 
                      idUsuario, e.getMessage());
        }

        log.info("Cita/Orden creada exitosamente: ID={}", ordenGuardada.getId());
        return convertirADTO(ordenGuardada);
    }

    public List<OrdenDTO> listarTodas() {
        log.info("Recuperando listado histórico de todas las órdenes");
        return ordenRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public OrdenDTO buscarPorId(Long id) {
        log.info("Buscando orden específica por ID={}", id);
        return ordenRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> {
                    log.warn("Error en consulta: La orden con ID={} no existe", id);
                    return new RecursoNoEncontradoException("Orden no encontrada: " + id);
                });
    }

    public List<OrdenDTO> buscarPorUsuario(Long idUsuario) {
        log.info("Buscando órdenes del usuario ID={}", idUsuario);
        try {
            userClient.getUserById(idUsuario);
            log.info("Validación de usuario ID={} correcta para extracción de órdenes", idUsuario);
        } catch (FeignException.NotFound e) {
            log.warn("Fallo de consulta: Intento de listar órdenes para un usuario inexistente ID={}", idUsuario);
            throw new RecursoNoEncontradoException("El usuario especificado no existe.");
        } catch (FeignException e) {
            log.error("Falla de red con Usuarios. Continuando por tolerancia a fallos en lectura: {}", e.getMessage());
            throw new ServicioNoDisponibleException("El servicio de usuarios no está disponible.");
        }

        return ordenRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public OrdenDTO actualizarEstado(Long id, String nuevoEstado) {
        log.info("Solicitud para actualizar estado de la orden ID={} a '{}'", id, nuevoEstado);
        return ordenRepository.findById(id).map(o -> {
            o.setEstado(nuevoEstado.toUpperCase());
            Orden actualizada = ordenRepository.save(o);
            log.info("Evento de negocio: Estado de orden ID={} modificado con éxito", id);
            return convertirADTO(actualizada);
        }).orElseThrow(() -> {
            log.warn("Fallo de actualización: La orden con ID={} no existe", id);
            return new RecursoNoEncontradoException("Orden no encontrada: " + id);
        });
    }

    public boolean eliminarOrden(Long id) {
        log.info("Solicitud para eliminar físicamente la orden ID={}", id);
        if (ordenRepository.existsById(id)) {
            ordenRepository.deleteById(id);
            log.info("Evento de negocio: Orden ID={} eliminada correctamente de la base de datos", id);
            return true;
        }
        log.warn("Fallo de eliminación: La orden ID={} no existe en los registros", id);
        return false;
    }

    private OrdenDTO convertirADTO(Orden o) {
        return new OrdenDTO(o.getId(), o.getIdUsuario(), o.getFechaCreacion(), o.getTotal(), o.getEstado());
    }
}
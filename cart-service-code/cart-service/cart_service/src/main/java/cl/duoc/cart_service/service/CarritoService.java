package cl.duoc.cart_service.service;

import cl.duoc.cart_service.client.CatalogClient;
import cl.duoc.cart_service.client.InventoryClient;
import cl.duoc.cart_service.dto.CarritoCreateDTO;
import cl.duoc.cart_service.dto.CarritoDTO;
import cl.duoc.cart_service.dto.InventoryDTO;
import cl.duoc.cart_service.dto.LibroDTO;
import cl.duoc.cart_service.exception.RecursoNoEncontradoException;
import cl.duoc.cart_service.exception.ServicioNoDisponibleException;
import cl.duoc.cart_service.model.Carrito;
import cl.duoc.cart_service.repository.CarritoRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    private static final Logger log = LoggerFactory.getLogger(CarritoService.class);

    private final CarritoRepository carritoRepository;

    @Autowired
    private CatalogClient catalogClient; 

    @Autowired
    private InventoryClient inventoryClient; 

    public CarritoService(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public CarritoDTO agregarItem(CarritoCreateDTO dto) {
        log.info("Iniciando proceso para agregar ítem. Libro ID: {} para Usuario ID: {}", dto.getIdLibro(), dto.getIdUsuario());

        log.info("Solicitando validación de existencia para libro ID={} al servicio Catalog", dto.getIdLibro());
        LibroDTO libro;
        try {
            libro = catalogClient.buscarPorId(dto.getIdLibro());
            log.info("Libro confirmado por Catalog: '{}'", libro.getTitulo());
        } catch (FeignException.NotFound e) {
            log.warn("Servicio Catalog respondió: El libro ID={} no existe", dto.getIdLibro());
            throw new RecursoNoEncontradoException("No se puede agregar al carrito: El libro especificado no existe.");
        } catch (FeignException e) {
            log.error("Error al consultar servicio Catalog: {}", e.getMessage());
            throw new ServicioNoDisponibleException("Servicio de catálogo no disponible para verificar el libro.");
        }

        log.info("Solicitando stock del libro ID={} al servicio Inventory", dto.getIdLibro());
        InventoryDTO inventario;
        try {
            inventario = inventoryClient.buscarPorIdLibro(dto.getIdLibro());
            log.info("Stock recibido de Inventory para libro ID={}: disponible={}", dto.getIdLibro(), inventario.getStock());
        } catch (FeignException.NotFound e) {
            log.warn("Servicio Inventory respondió: No existe registro de inventario para libro ID={}", dto.getIdLibro());
            throw new RecursoNoEncontradoException("No se puede agregar al carrito: El inventario de este libro no está registrado.");
        } catch (FeignException e) {
            log.error("Error al consultar servicio Inventory: {}", e.getMessage());
            throw new ServicioNoDisponibleException("Servicio de inventario no disponible para verificar existencias.");
        }
        
        if (inventario.getStock() < dto.getCantidad()) {
            log.warn("Stock insuficiente para libro '{}'. Solicitado: {}, Disponible: {}", libro.getTitulo(), dto.getCantidad(), inventario.getStock());
            throw new RuntimeException("No hay stock suficiente para agregar al carrito.");
        }

        Carrito existente = carritoRepository.findByIdUsuarioAndIdLibro(dto.getIdUsuario(), dto.getIdLibro());
        
        if (existente != null) {
            log.info("El libro ID={} ya existe en el carrito del usuario {}. Sumando cantidad...", dto.getIdLibro(), dto.getIdUsuario());
            existente.setCantidad(existente.getCantidad() + dto.getCantidad());
            Carrito guardado = carritoRepository.save(existente);
            log.info("Cantidad actualizada con éxito en ítem carrito ID={}", guardado.getId());
            return convertirADTO(guardado);
        }

        Carrito nuevo = new Carrito();
        nuevo.setIdUsuario(dto.getIdUsuario());
        nuevo.setIdLibro(dto.getIdLibro());
        nuevo.setCantidad(dto.getCantidad());
        
        Carrito guardadoNuevo = carritoRepository.save(nuevo);
        log.info("Nuevo ítem registrado con éxito en el carrito con ID={}", guardadoNuevo.getId());
        return convertirADTO(guardadoNuevo);
    }

    public List<CarritoDTO> obtenerPorUsuario(Long idUsuario) {
        log.info("Solicitando listado de carrito para el usuario ID={}", idUsuario);
        return carritoRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void vaciarCarrito(Long idUsuario) {
        log.info("Solicitud recibida para vaciar por completo el carrito del usuario ID={}", idUsuario);
        carritoRepository.deleteByIdUsuario(idUsuario);
        log.info("Carrito del usuario ID={} eliminado exitosamente.", idUsuario);
    }

    public boolean eliminarItem(Long id) {
        log.info("Intentando eliminar ítem de carrito ID={}", id);
        if (carritoRepository.existsById(id)) {
            carritoRepository.deleteById(id);
            log.info("Ítem de carrito ID={} eliminado exitosamente", id);
            return true;
        }
        log.warn("No se pudo eliminar: El ítem de carrito ID={} no existe", id);
        return false;
    }

    public CarritoDTO actualizarCantidad(Long id, Integer nuevaCantidad) {
        log.info("Solicitud para actualizar cantidad a {} en ítem carrito ID={}", nuevaCantidad, id);
        return carritoRepository.findById(id).map(c -> {
            
            log.info("Verificando stock en Inventory para reajuste de ítem carrito ID={} (Libro ID={})", id, c.getIdLibro());
            InventoryDTO inv;
            try {
                inv = inventoryClient.buscarPorIdLibro(c.getIdLibro());
            } catch (FeignException.NotFound e) {
                log.warn("No se pudo verificar stock: Inventario no registrado para libro ID={}", c.getIdLibro());
                throw new RecursoNoEncontradoException("Error al actualizar: El registro de inventario ya no existe.");
            } catch (FeignException e) {
                log.error("Falla de comunicación con Inventory al reajustar cantidad: {}", e.getMessage());
                throw new ServicioNoDisponibleException("No se puede validar el stock porque el servicio de inventario falló.");
            }

            if (inv.getStock() < nuevaCantidad) {
                log.warn("Actualización denegada para ítem {}. Cantidad pedida: {}, Stock real: {}", id, nuevaCantidad, inv.getStock());
                throw new RuntimeException("No puedes actualizar a esa cantidad, supera el stock (" + inv.getStock() + ")");
            }

            c.setCantidad(nuevaCantidad);
            Carrito actualizado = carritoRepository.save(c);
            log.info("Cantidad reajustada con éxito a {} para ítem carrito ID={}", nuevaCantidad, id);
            return convertirADTO(actualizado);
        }).orElseThrow(() -> {
            log.warn("Falla al actualizar: El ítem de carrito ID={} no existe", id);
            return new RecursoNoEncontradoException("Item de carrito no encontrado");
        });
    }

    private CarritoDTO convertirADTO(Carrito c) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(c.getId());
        dto.setIdUsuario(c.getIdUsuario());
        dto.setIdLibro(c.getIdLibro());
        dto.setCantidad(c.getCantidad());

        try {
            LibroDTO libro = catalogClient.buscarPorId(c.getIdLibro());
            if (libro != null) {
                dto.setTituloLibro(libro.getTitulo());
                dto.setPrecioUnitario(libro.getPrecio());
                dto.setSubtotal(libro.getPrecio() * c.getCantidad());
            }
        } catch (FeignException.NotFound e) {
            log.warn("Mapeo parcial: El libro ID={} no existe en Catalog para renderizar detalles", c.getIdLibro());
            dto.setTituloLibro("Libro No Encontrado en Catálogo");
            dto.setPrecioUnitario(0.0);
            dto.setSubtotal(0.0);
        } catch (Exception e) {
            log.error("Error de comunicación con Catalog al mapear DTO para libro ID={}: {}", c.getIdLibro(), e.getMessage());
            dto.setTituloLibro("Libro No Disponible Temporalmente");
            dto.setPrecioUnitario(0.0);
            dto.setSubtotal(0.0);
        }

        return dto;
    }
}
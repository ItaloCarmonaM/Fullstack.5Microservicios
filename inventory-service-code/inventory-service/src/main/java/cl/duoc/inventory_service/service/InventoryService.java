package cl.duoc.inventory_service.service;

import cl.duoc.inventory_service.client.CatalogClient;
import cl.duoc.inventory_service.dto.InventoryCreateDTO;
import cl.duoc.inventory_service.dto.InventoryDTO;
import cl.duoc.inventory_service.dto.LibroDTO;
import cl.duoc.inventory_service.exception.RecursoNoEncontradoException;
import cl.duoc.inventory_service.exception.ServicioNoDisponibleException;
import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.repository.InventoryRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CatalogClient catalogClient;

    public InventoryDTO crearInventory(InventoryCreateDTO request) {
        log.info("Solicitando validación de existencia para libro ID={} al servicio Catalog", request.getIdLibro());

        LibroDTO libro;
        try {
            libro = catalogClient.getLibroById(request.getIdLibro());
            log.info("Libro recibido correctamente de Catalog: ID={}, Título='{}'", libro.getId(), libro.getTitulo());
        } catch (FeignException.NotFound e) {
            log.warn("Servicio Catalog respondió: El libro ID={} no existe", request.getIdLibro());
            throw new RecursoNoEncontradoException("No se puede crear el inventario: El libro especificado no existe.");
        } catch (FeignException e) {
            log.error("Error crítico de comunicación con el servicio Catalog: {}", e.getMessage());
            throw new ServicioNoDisponibleException("El servicio de Catálogo no se encuentra disponible para validar el libro.");
        }

        Inventory inventory = new Inventory();
        inventory.setIdLibro(libro.getId());
        inventory.setStock(request.getStock());
        
        Inventory guardado = inventoryRepository.save(inventory);
        log.info("Inventario registrado exitosamente: ID={}, Libro ID={}, Stock={}", guardado.getId(), guardado.getIdLibro(), guardado.getStock());

        return new InventoryDTO(
            guardado.getId(), 
            guardado.getIdLibro(), 
            guardado.getStock()
        );
    }

    public List<InventoryDTO> listarInventories() {
        log.info("Solicitando listado completo de todos los inventarios");
        return inventoryRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public InventoryDTO buscarPorId(Long id) {
        log.info("Buscando registro de inventario por ID={}", id);
        return inventoryRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> {
                    log.warn("Intento fallido de buscar inventario: ID={} no existe", id);
                    return new RecursoNoEncontradoException("Registro de inventario no encontrado con el ID: " + id);
                });
    }

    public InventoryDTO buscarPorIdLibro(Long idLibro) {
        log.info("Buscando stock de inventario para el Libro ID={}", idLibro);
        Inventory inv = inventoryRepository.findByIdLibro(idLibro);
        if (inv == null) {
            log.warn("No se encontró stock registrado en la base de datos para el Libro ID={}", idLibro);
            throw new RecursoNoEncontradoException("No hay stock registrado para el libro con ID: " + idLibro);
        }
        return convertirADTO(inv);
    }

    public InventoryDTO actualizarStock(Long id, InventoryCreateDTO dto) {
        log.info("Solicitud recibida para actualizar stock en inventario ID={} a un nuevo valor de {}", id, dto.getStock());
        return inventoryRepository.findById(id).map(inventory -> {
            inventory.setStock(dto.getStock());
            Inventory actualizado = inventoryRepository.save(inventory);
            log.info("Stock actualizado con éxito para inventario ID={}. Nuevo stock={}", id, actualizado.getStock());
            return convertirADTO(actualizado);
        }).orElseThrow(() -> {
            log.warn("Falla al actualizar: El registro de inventario ID={} no existe", id);
            return new RecursoNoEncontradoException("No se puede actualizar: Registro de inventario no encontrado con el ID: " + id);
        });
    }

    public boolean eliminarInventory(Long id) {
        log.info("Intentando eliminar registro de inventario ID={}", id);
        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
            log.info("Registro de inventario ID={} eliminado exitosamente", id);
            return true;
        }
        log.warn("No se pudo eliminar: El registro de inventario ID={} no existe", id);
        return false;
    }

    private InventoryDTO convertirADTO(Inventory inventory) {
        return new InventoryDTO(inventory.getId(), inventory.getIdLibro(), inventory.getStock());
    }
}
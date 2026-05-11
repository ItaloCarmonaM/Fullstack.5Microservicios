package cl.duoc.inventory_service.service;

import cl.duoc.inventory_service.dto.InventoryCreateDTO;
import cl.duoc.inventory_service.dto.InventoryDTO;
import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryDTO crearInventory(InventoryCreateDTO dto) {
        Inventory inventory = new Inventory();
        inventory.setIdLibro(dto.getIdLibro());
        inventory.setStock(dto.getStock());
        return convertirADTO(inventoryRepository.save(inventory));
    }

    public List<InventoryDTO> listarInventories() {
        return inventoryRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public InventoryDTO buscarPorId(Long id) {
        return inventoryRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new InventoryNotFoundException(id));
    }

    public InventoryDTO buscarPorIdLibro(Long idLibro) {
        Inventory inv = inventoryRepository.findByIdLibro(idLibro);
        if (inv == null) throw new InventoryNotFoundException("No hay stock registrado para el libro: " + idLibro);
        return convertirADTO(inv);
    }

    public InventoryDTO actualizarStock(Long id, InventoryCreateDTO dto) {
        return inventoryRepository.findById(id).map(inventory -> {
            inventory.setStock(dto.getStock());
            return convertirADTO(inventoryRepository.save(inventory));
        }).orElseThrow(() -> new InventoryNotFoundException(id));
    }

    public boolean eliminarInventory(Long id) {
        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private InventoryDTO convertirADTO(Inventory inventory) {
        return new InventoryDTO(inventory.getId(), inventory.getIdLibro(), inventory.getStock());
    }

    public static class InventoryNotFoundException extends RuntimeException {
        public InventoryNotFoundException(Object id) { super("Inventario no encontrado: " + id); }
    }
}
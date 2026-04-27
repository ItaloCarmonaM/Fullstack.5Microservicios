package cl.duoc.inventory_service.service;
import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.repository.InventoryRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> listarInventories() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> buscarInventoryPorId(Long id) {
        return inventoryRepository.findById(id);
    }

    public List<Inventory> buscarInventoryPorIdLibro(Long idLibro) {
        Inventory inventory = inventoryRepository.findByIdLibro(idLibro);
        return inventory != null ? List.of(inventory) : List.of();
    }

    public Inventory crearInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Optional<Inventory> actualizarInventory(Long id, Inventory updatedInventory) {
        return inventoryRepository.findById(id).map(inventory -> {
            inventory.setIdLibro(updatedInventory.getIdLibro());
            inventory.setStock(updatedInventory.getStock());
            return inventoryRepository.save(inventory);
        });
    }

    public boolean eliminarInventory(Long id) {
        if (inventoryRepository.existsById(id)) {
            inventoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
}

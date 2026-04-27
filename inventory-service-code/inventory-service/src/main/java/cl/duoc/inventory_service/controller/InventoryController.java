package cl.duoc.inventory_service.controller;
import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.service.InventoryService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/v1/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<Inventory> crearInventory(@Valid @RequestBody Inventory inventory) {
        Inventory nuevoInventario = inventoryService.crearInventory(inventory);
        return new ResponseEntity<>(nuevoInventario, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Inventory>> obtenerInventario(){
        List<Inventory> inventarios = inventoryService.listarInventories();
        return new ResponseEntity<>(inventarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> buscarPorId(@PathVariable Long id) {
        Optional<Inventory> inventory = inventoryService.buscarInventoryPorId(id);
        if(inventory.isPresent()) {
            return ResponseEntity.ok(inventory.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Buscar inventory por idLibro
    @GetMapping("/idLibro/{idLibro}")
    public ResponseEntity<List<Inventory>> buscarPorIdLibro(@PathVariable Long idLibro) {
        List<Inventory> inventarios = inventoryService.buscarInventoryPorIdLibro(idLibro);
        if(!inventarios.isEmpty()) {
            return ResponseEntity.ok(inventarios);
        } else {    
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventory(@PathVariable Long id) {
        boolean eliminado = inventoryService.eliminarInventory(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> actualizarInventory(@PathVariable Long id, @Valid @RequestBody Inventory inventoryActualizada) {
        Optional<Inventory> updatedInventory = inventoryService.actualizarInventory(id, inventoryActualizada);
        if (updatedInventory.isPresent()) {
            return ResponseEntity.ok(updatedInventory.get());
        } else {
            return ResponseEntity.notFound().build();  
        }
    }
}

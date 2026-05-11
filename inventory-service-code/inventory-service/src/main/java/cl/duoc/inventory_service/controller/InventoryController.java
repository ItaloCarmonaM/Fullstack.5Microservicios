package cl.duoc.inventory_service.controller;

import cl.duoc.inventory_service.dto.InventoryCreateDTO;
import cl.duoc.inventory_service.dto.InventoryDTO;
import cl.duoc.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> crearInventory(@Valid @RequestBody InventoryCreateDTO dto) {
        return new ResponseEntity<>(inventoryService.crearInventory(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> obtenerTodos() {
        return ResponseEntity.ok(inventoryService.listarInventories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.buscarPorId(id));
    }

    @GetMapping("/idLibro/{idLibro}")
    public ResponseEntity<InventoryDTO> buscarPorIdLibro(@PathVariable Long idLibro) {
        return ResponseEntity.ok(inventoryService.buscarPorIdLibro(idLibro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> actualizarStock(@PathVariable Long id, @Valid @RequestBody InventoryCreateDTO dto) {
        return ResponseEntity.ok(inventoryService.actualizarStock(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return inventoryService.eliminarInventory(id) 
            ? ResponseEntity.noContent().build() 
            : ResponseEntity.notFound().build();
    }
}
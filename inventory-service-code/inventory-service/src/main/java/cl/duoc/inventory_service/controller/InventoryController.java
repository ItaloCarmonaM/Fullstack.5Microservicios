package cl.duoc.inventory_service.controller;

import cl.duoc.inventory_service.dto.InventoryCreateDTO;
import cl.duoc.inventory_service.dto.InventoryDTO;
import cl.duoc.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(
    name = "Inventario",
    description = "Gestión de inventarios de productos"
)
@RestController
@RequestMapping("/api/v2/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(
        summary = "Crear inventario",
        description = "Crea un nuevo registro de inventario para un libro"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Inventario creado correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        )
    })
    @PostMapping
    public ResponseEntity<InventoryDTO> crearInventory(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos necesarios para crear un inventario",
            required = true
        )
        @Valid
        @RequestBody InventoryCreateDTO dto
    ) {
        return new ResponseEntity<>(
            inventoryService.crearInventory(dto),
            HttpStatus.CREATED
        );
    }

    @Operation(
        summary = "Listar inventarios",
        description = "Obtiene todos los registros de inventario existentes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista obtenida correctamente"
        )
    })
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> obtenerTodos() {
        return ResponseEntity.ok(inventoryService.listarInventories());
    }

    @Operation(
        summary = "Buscar un inventario por ID",
        description = "Obtiene un inventario  utilizando su id"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Inventario encontrado"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Inventario no encontrado"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> buscarPorId(
        @Parameter(
            description = "ID único del inventario",
            required = true,
            example = "1"
        )
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(inventoryService.buscarPorId(id));
    }

    @Operation(
        summary = "Buscar inventario por ID de libro",
        description = "Obtiene el inventario de un libro específico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Inventario encontrado"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No existe inventario para el libro"
        )
    })
    @GetMapping("/idLibro/{idLibro}")
    public ResponseEntity<InventoryDTO> buscarPorIdLibro(
        @Parameter(
            description = "ID del libro asociado al inventario",
            required = true,
            example = "10"
        )
        @PathVariable Long idLibro
    ) {
        return ResponseEntity.ok(
            inventoryService.buscarPorIdLibro(idLibro)
        );
    }

    @Operation(
        summary = "Actualizar el stock de un producto",
        description = "Actualiza la información de stock de un inventario existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Stock actualizado correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Inventario no encontrado"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> actualizarStock(
        @Parameter(
            description = "ID del inventario a actualizar",
            required = true,
            example = "1"
        )
        @PathVariable Long id,

        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nuevos datos del inventario",
            required = true
        )
        @Valid
        @RequestBody InventoryCreateDTO dto
    ) {
        return ResponseEntity.ok(
            inventoryService.actualizarStock(id, dto)
        );
    }

    @Operation(
        summary = "Eliminar inventario",
        description = "Elimina un registro de inventario por su identificador"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Inventario eliminado correctamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Inventario no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(
            description = "ID del inventario a eliminar",
            required = true,
            example = "1"
        )
        @PathVariable Long id
    ) {
        return inventoryService.eliminarInventory(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
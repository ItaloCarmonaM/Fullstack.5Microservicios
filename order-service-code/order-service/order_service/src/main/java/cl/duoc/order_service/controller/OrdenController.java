package cl.duoc.order_service.controller;

import cl.duoc.order_service.dto.OrdenDTO;
import cl.duoc.order_service.dto.OrdenCreateDTO; 
import cl.duoc.order_service.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Órdenes", description = "Endpoints para la creación, consulta, modificación de estado y eliminación de órdenes de compra")
@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @Operation(summary = "Crear una nueva orden", 
               description = "Inicia el flujo: verifica la existencia del usuario, e inspecciona el carrito de compras, genera el registro de la orden consolidada y vacía el carrito.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Orden creada y procesada con éxito"),
        @ApiResponse(responseCode = "400", description = "Payload inválido o datos de entrada con mal formato"),
        @ApiResponse(responseCode = "404", description = "El usuario especificado o su carrito asociado no existen"),
        @ApiResponse(responseCode = "503", description = "Falla de comunicación por caída de servicios externos")
    })
    @PostMapping
    public ResponseEntity<OrdenDTO> crearOrden(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura JSON con los datos base de la compra")
            @jakarta.validation.Valid @RequestBody OrdenCreateDTO dto) {
        return new ResponseEntity<>(ordenService.crearOrden(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las órdenes", description = "Retorna el historial completo de todas las órdenes registradas en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Listado completo obtenido con éxito")
    @GetMapping
    public ResponseEntity<List<OrdenDTO>> obtenerTodas() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    @Operation(summary = "Buscar orden por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orden encontrada"),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún registro con el ID provisto")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> buscarPorId(
            @Parameter(description = "ID único de la orden", required = true, example = "101")
            @PathVariable Long id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    @Operation(summary = "Buscar órdenes asociadas a un usuario específico", 
               description = "Valida si el usuario existe y extrae todas sus órdenes de compra.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Órdenes localizadas con éxito"),
        @ApiResponse(responseCode = "204", description = "El usuario existe pero no registra órdenes previas"),
        @ApiResponse(responseCode = "404", description = "El ID del usuario no existe en el sistema")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<OrdenDTO>> buscarPorUsuario(
            @Parameter(description = "ID del usuario", required = true, example = "5")
            @PathVariable Long idUsuario) {
        List<OrdenDTO> ordenes = ordenService.buscarPorUsuario(idUsuario);
        return ordenes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Cambiar el estado de una orden", description = "Permite transicionar de estados (Ej: de PROCESADA a ENVIADA o ENTREGADA).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado de la orden actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "La orden con el ID provisto no fue encontrada")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenDTO> cambiarEstado(
            @Parameter(description = "ID de la orden a modificar", required = true, example = "101") @PathVariable Long id, 
            @Parameter(description = "Nuevo estado para la orden", required = true, example = "ENVIADA") @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(ordenService.actualizarEstado(id, nuevoEstado));
    }

    @Operation(summary = "Eliminar una orden del registro")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Orden removida exitosamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "La orden no se pudo eliminar porque el ID no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(
            @Parameter(description = "ID de la orden a remover", required = true, example = "101")
            @PathVariable Long id) {
        boolean eliminado = ordenService.eliminarOrden(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
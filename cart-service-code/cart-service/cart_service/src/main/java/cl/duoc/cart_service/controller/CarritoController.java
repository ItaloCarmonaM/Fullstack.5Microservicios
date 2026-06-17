package cl.duoc.cart_service.controller;

import cl.duoc.cart_service.dto.CarritoCreateDTO;
import cl.duoc.cart_service.dto.CarritoDTO;
import cl.duoc.cart_service.service.CarritoService;
import jakarta.validation.Valid;

// Imports de Swagger añadidos
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Carritos", description = "Operaciones de gestión del carrito de compras para usuarios")
@RestController
@RequestMapping("/api/v1/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    // POST /api/v1/carritos
    @Operation(summary = "Agregar un ítem al carrito", 
               description = "Registra un nuevo libro en el carrito de un usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Ítem agregado o incrementado con éxito en el carrito"),
        @ApiResponse(responseCode = "404", description = "El usuario, libro o inventario solicitado no existe"),
        @ApiResponse(responseCode = "503", description = "Error de comunicación con microservicios remotos")
    })
    @PostMapping
    public ResponseEntity<CarritoDTO> agregarItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos estructurados para la asignación del ítem")
            @Valid @RequestBody CarritoCreateDTO dto) {
        return new ResponseEntity<>(carritoService.agregarItem(dto), HttpStatus.CREATED);
    }

    // GET /api/v1/carritos
    @Operation(summary = "Listar todos los ítems de carritos", 
               description = "Retorna una lista completa de todos los carritos de todos los usuarios del sistema.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<CarritoDTO>> obtenerTodos() {
        return ResponseEntity.ok(carritoService.listarCarritos());
    }

    // GET /api/v1/carritos/{id}
    @Operation(summary = "Buscar un carrito específico por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carrito encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún carrito con el ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> buscarPorId(
            @Parameter(description = "ID único del carrito", required = true, example = "10")
            @PathVariable Long id) {
        return ResponseEntity.ok(carritoService.buscarPorId(id));
    }

    // GET /api/v1/carritos/usuario/{idUsuario}
    @Operation(summary = "Obtener el carrito de un usuario", 
               description = "Valida al usuario e inspecciona la base de datos para retornar todos los libros que tiene en el carrito  actualmente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ítems del carrito devueltos correctamente"),
        @ApiResponse(responseCode = "204", description = "El usuario existe pero no tiene productos cargados en su carrito"),
        @ApiResponse(responseCode = "404", description = "El ID del usuario no existe en el sistema")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<CarritoDTO>> obtenerCarrito(
            @Parameter(description = "ID del usuario para obtener su carrito", required = true, example = "1")
            @PathVariable Long idUsuario) {
        List<CarritoDTO> items = carritoService.obtenerPorUsuario(idUsuario);
        return items.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(items);
    }

    // PATCH /api/v1/carritos/{id}
    @Operation(summary = "Actualizar la cantidad de un ítem existente", 
               description = "Modifica  las unidades de un ítem de carrito, validando con el stock del Inventario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cantidad modificada correctamente en el carrito"),
        @ApiResponse(responseCode = "404", description = "El ítem de carrito o el inventario del libro ya no existen en el sistema")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CarritoDTO> actualizarCantidad(
            @Parameter(description = "ID del ítem a modificar", required = true, example = "10") @PathVariable Long id, 
            @Parameter(description = "Nueva cantidad total de unidades para este ítem", required = true, example = "5") @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(id, cantidad));
    }

    // DELETE /api/v1/carritos/{id}
    @Operation(summary = "Eliminar un ítem del carrito por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "El ítem se eliminó correctamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "El ítem no se eliminó porque el ID no existe en el carrito")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(
            @Parameter(description = "ID único del ítem a eliminar", required = true, example = "10")
            @PathVariable Long id) {
        return carritoService.eliminarItem(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // DELETE /api/v1/carritos/usuario/{idUsuario}
    @Operation(summary = "Vaciar por completo el carrito de un usuario", 
               description = "Elimina el carrito perteneciente al usuario especificado.")
    @ApiResponse(responseCode = "204", description = "El carrito asociado al ID del usuario se vació correctamente (No Content)")
    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> vaciar(
            @Parameter(description = "ID único del usuario cuyo carrito será vaciado", required = true, example = "1")
            @PathVariable Long idUsuario) {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
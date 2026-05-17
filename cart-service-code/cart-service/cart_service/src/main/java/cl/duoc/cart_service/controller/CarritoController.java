package cl.duoc.cart_service.controller;

import cl.duoc.cart_service.dto.CarritoCreateDTO;
import cl.duoc.cart_service.dto.CarritoDTO;
import cl.duoc.cart_service.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping
    public ResponseEntity<CarritoDTO> agregarItem(@Valid @RequestBody CarritoCreateDTO dto) {
        return new ResponseEntity<>(carritoService.agregarItem(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CarritoDTO>> obtenerTodos() {
        return ResponseEntity.ok(carritoService.listarCarritos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carritoService.buscarPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<CarritoDTO>> obtenerCarrito(@PathVariable Long idUsuario) {
        List<CarritoDTO> items = carritoService.obtenerPorUsuario(idUsuario);
        return items.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(items);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CarritoDTO> actualizarCantidad(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(id, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        return carritoService.eliminarItem(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> vaciar(@PathVariable Long idUsuario) {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
package cl.duoc.order_service.controller;

import cl.duoc.order_service.dto.OrdenCreateDTO;
import cl.duoc.order_service.dto.OrdenDTO;
import cl.duoc.order_service.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    // Crear orden
    @PostMapping
    public ResponseEntity<OrdenDTO> crearOrden(@Valid @RequestBody OrdenCreateDTO dto) {
        return new ResponseEntity<>(ordenService.crearOrden(dto), HttpStatus.CREATED);
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<OrdenDTO>> obtenerTodas() {
        return ResponseEntity.ok(ordenService.listarTodas());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    // Buscar por Usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<OrdenDTO>> buscarPorUsuario(@PathVariable Long idUsuario) {
        List<OrdenDTO> ordenes = ordenService.buscarPorUsuario(idUsuario);
        return ordenes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ordenes);
    }

    // Actualizar estado (Cambiado de PATCH a PUT por requerimiento académico)
    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenDTO> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(ordenService.actualizarEstado(id, nuevoEstado));
    }

    // Eliminar orden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrden(@PathVariable Long id) {
        boolean eliminado = ordenService.eliminarOrden(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
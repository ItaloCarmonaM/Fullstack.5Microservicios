package cl.duoc.catalog_service.controller;

import cl.duoc.catalog_service.dto.LibroCreateDTO;
import cl.duoc.catalog_service.dto.LibroDTO;
import cl.duoc.catalog_service.service.LibroService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    // Registrar un nuevo libro: Recibe CreateDTO y devuelve LibroDTO
    @PostMapping
    public ResponseEntity<LibroDTO> registrarLibro(@Valid @RequestBody LibroCreateDTO dto) {
        LibroDTO nuevoLibro = libroService.registrarLibro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
    }

    // Listar todos los libros: Devuelve lista de DTOs
    @GetMapping
    public ResponseEntity<List<LibroDTO>> listaLibros() {
        List<LibroDTO> libros = libroService.listaLibros();
        return ResponseEntity.ok(libros);
    }

    // Buscar libro por id: Devuelve DTO
    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> buscarPorId(@PathVariable Long id) {
        // El service ya lanza LibroNotFoundException si no existe
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

    // Buscar libro por autor
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LibroDTO>> buscarPorAutor(@PathVariable String autor) {
        List<LibroDTO> libros = libroService.buscarPorAutor(autor);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    // Buscar libro por título
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LibroDTO>> buscarPorTitulo(@PathVariable String titulo) {
        List<LibroDTO> libros = libroService.buscarPorTitulo(titulo);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    // Buscar por precio menor
    @GetMapping("/precio/menor/{precio}")
    public ResponseEntity<List<LibroDTO>> buscarPorPrecioMenor(@PathVariable Double precio) {
        List<LibroDTO> libros = libroService.buscarPorPrecioMenor(precio);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    // Buscar por precio mayor
    @GetMapping("/precio/mayor/{precio}")
    public ResponseEntity<List<LibroDTO>> buscarPorPrecioMayor(@PathVariable Double precio) {
        List<LibroDTO> libros = libroService.buscarPorPrecioMayor(precio);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    // Buscar por precio entre
    @GetMapping("/precio/entre")
    public ResponseEntity<List<LibroDTO>> buscarPorPrecioEntre(@RequestParam Double precioMin,
                                                               @RequestParam Double precioMax) {
        if (precioMin > precioMax) {
            return ResponseEntity.badRequest().build();
        }
        List<LibroDTO> libros = libroService.buscarPorPrecioEntre(precioMin, precioMax);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    // Buscar por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<LibroDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<LibroDTO> libros = libroService.buscarPorCategoria(categoria);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    // Buscar por editorial
    @GetMapping("/editorial/{editorial}")
    public ResponseEntity<List<LibroDTO>> buscarPorEditorial(@PathVariable String editorial) {
        List<LibroDTO> libros = libroService.buscarPorEditorial(editorial);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    // Eliminar libro por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        boolean eliminado = libroService.eliminarLibro(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Actualizar libro por id: Recibe CreateDTO y devuelve LibroDTO
    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> actualizarLibro(@PathVariable Long id, 
                                                   @Valid @RequestBody LibroCreateDTO dto) {
        LibroDTO actualizado = libroService.actualizarLibro(id, dto);
        return ResponseEntity.ok(actualizado);
    }
}
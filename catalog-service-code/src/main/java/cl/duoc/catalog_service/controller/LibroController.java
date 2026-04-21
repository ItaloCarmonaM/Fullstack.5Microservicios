package cl.duoc.catalog_service.controller;

import cl.duoc.catalog_service.model.Libro;
import cl.duoc.catalog_service.service.LibroService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/libros")
public class LibroController {
    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    // Registrar un nuevo libro
    @PostMapping
    public ResponseEntity<Libro> registrarLibro(@Valid @RequestBody Libro libro) {
        Libro nuevoLibro = libroService.registrarLibro(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
    }

    // Listar todos los libros
    @GetMapping
    public ResponseEntity<List<Libro>> listaLibros() {
        List<Libro> libros = libroService.listaLibros();
        return ResponseEntity.ok(libros);
    }

    // Buscar libro por id
    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Long id) {
        Optional<Libro> libro = libroService.buscarPorId(id);
        if(libro.isPresent()) {
            return ResponseEntity.ok(libro.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar libro por autor
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<Libro>> buscarPorAutor(@PathVariable String autor) {
        List<Libro> libros = libroService.buscarPorAutor(autor);

        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(libros);
    }

    // Buscar libro por título
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@PathVariable String titulo) {
        List<Libro> libros = libroService.buscarPorTitulo(titulo);

        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(libros);
    }

    // Buscar libro por precio menor a un valor
    @GetMapping("/precio/menor/{precio}")
    public ResponseEntity<List<Libro>> buscarPorPrecioMenor(@PathVariable Double precio) {
        List<Libro> libros = libroService.buscarPorPrecioMenor(precio);

        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(libros);
    }

    // Buscar libro por precio mayor a un valor
    @GetMapping("/precio/mayor/{precio}")
    public ResponseEntity<List<Libro>> buscarPorPrecioMayor(@PathVariable Double precio) {
        List<Libro> libros = libroService.buscarPorPrecioMayor(precio);

        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(libros);
    }

    // Buscar libro por precio entre dos valores (usarsería con esta estructura, por ejemplo: /precio/entre?precioMin=10&precioMax=50)
    @GetMapping("/precio/entre")
    public ResponseEntity<List<Libro>> buscarPorPrecioEntre(@RequestParam Double precioMin, @RequestParam Double precioMax) {
        List<Libro> libros = libroService.buscarPorPrecioEntre(precioMin, precioMax);
        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        if (precioMin > precioMax) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(libros);
    }

    // Eliminar libro por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        boolean eliminado = libroService.eliminarLibro(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar libro por id
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @Valid @RequestBody Libro libroActualizado) {
        Libro libro = libroService.actualizarLibro(id, libroActualizado);
        if (libro != null) {
            return ResponseEntity.ok(libro);
        } else {
            return ResponseEntity.notFound().build();  
        }
    }
}

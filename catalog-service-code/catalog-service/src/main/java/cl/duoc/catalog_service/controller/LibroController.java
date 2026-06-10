package cl.duoc.catalog_service.controller;

import cl.duoc.catalog_service.dto.LibroCreateDTO;
import cl.duoc.catalog_service.dto.LibroDTO;
import cl.duoc.catalog_service.service.LibroService;
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

@Tag(name = "Libros", description = "Endpoints para consultas avanzadas del catálogo de libros")
@RestController
@RequestMapping("api/v2/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @Operation(summary = "Registrar nuevo libro", description = "Inserta un nuevo libro verificando las validaciones del DTO de entrada.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Libro registrado y guardado con éxito"),
        @ApiResponse(responseCode = "400", description = "Cuerpo de petición inválido o campos obligatorios vacíos")
    })
    @PostMapping
    public ResponseEntity<LibroDTO> registrarLibro(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Esquema JSON con los datos del libro a registrar", required = true)
        @Valid @RequestBody LibroCreateDTO dto
    ) {
        LibroDTO nuevoLibro = libroService.registrarLibro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);
    }

    @Operation(summary = "Listar catálogo completo", description = "Recupera todos los registros de libros almacenados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Colección recuperada de manera exitosa")
    @GetMapping
    public ResponseEntity<List<LibroDTO>> listaLibros() {
        List<LibroDTO> libros = libroService.listaLibros();
        return ResponseEntity.ok(libros);
    }

    @Operation(summary = "Buscar libro por su ID", description = "Obtiene los detalles de un libro en función a su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro encontrado correctamente"),
        @ApiResponse(responseCode = "404", description = "El ID especificado no coincide con ningún registro")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> buscarPorId(
        @Parameter(description = "Identificador numérico de base de datos", required = true, example = "1")
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

    @Operation(summary = "Buscar libros por autor", description = "Filtra el catálogo devolviendo coincidencias ignorando mayúsculas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultados encontrados"),
        @ApiResponse(responseCode = "204", description = "Operación exitosa pero no hay libros de dicho autor")
    })
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LibroDTO>> buscarPorAutor(
        @Parameter(description = "Nombre completo o parcial del escritor", required = true, example = "Katzenbach")
        @PathVariable String autor
    ) {
        List<LibroDTO> libros = libroService.buscarPorAutor(autor);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    @Operation(summary = "Buscar libros por título", description = "Filtra libros por el título ingresado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultados encontrados"),
        @ApiResponse(responseCode = "204", description = "No existen libros con ese título")
    })
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LibroDTO>> buscarPorTitulo(
        @Parameter(description = "Título exacto de la obra", required = true, example = "El Psicoanalista")
        @PathVariable String titulo
    ) {
        List<LibroDTO> libros = libroService.buscarPorTitulo(titulo);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    @Operation(summary = "Filtrar por precio máximo", description = "Retorna libros cuyo costo sea menor al valor entregado.")
    @GetMapping("/precio/menor/{precio}")
    public ResponseEntity<List<LibroDTO>> buscarPorPrecioMenor(
        @Parameter(description = "Umbral máximo de precio", required = true, example = "15000.0")
        @PathVariable Double precio
    ) {
        List<LibroDTO> libros = libroService.buscarPorPrecioMenor(precio);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    @Operation(summary = "Filtrar por precio mínimo", description = "Retorna libros cuyo costo sea mayor al valor entregado.")
    @GetMapping("/precio/mayor/{precio}")
    public ResponseEntity<List<LibroDTO>> buscarPorPrecioMayor(
        @Parameter(description = "Umbral mínimo de precio", required = true, example = "10000.0")
        @PathVariable Double precio
    ) {
        List<LibroDTO> libros = libroService.buscarPorPrecioMayor(precio);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    @Operation(summary = "Filtrar libros por rango de precios", description = "Muestra los libros cuyo precio se encuentre dentro del rango especificado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Obras dentro del rango localizadas"),
        @ApiResponse(responseCode = "400", description = "Error: El precio mínimo no puede superar al máximo"),
        @ApiResponse(responseCode = "204", description = "No existen registros en ese margen económico")
    })
    @GetMapping("/precio/entre")
    public ResponseEntity<List<LibroDTO>> buscarPorPrecioEntre(
        @Parameter(description = "Precio mínimo", required = true, example = "5000.0") @RequestParam Double precioMin,
        @Parameter(description = "Precio máximo", required = true, example = "25000.0") @RequestParam Double precioMax
    ) {
        if (precioMin > precioMax) {
            return ResponseEntity.badRequest().build();
        }
        List<LibroDTO> libros = libroService.buscarPorPrecioEntre(precioMin, precioMax);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    @Operation(summary = "Buscar libros por categoría", description = "Lista los libros pertenecientes a un mismo género.")
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<LibroDTO>> buscarPorCategoria(
        @Parameter(description = "Nombre de la categoría buscada", required = true, example = "Terror")
        @PathVariable String categoria
    ) {
        List<LibroDTO> libros = libroService.buscarPorCategoria(categoria);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    @Operation(summary = "Buscar libros por su casa editorial", description = "Muestra las publicaciones asociadas a una editorial.")
    @GetMapping("/editorial/{editorial}")
    public ResponseEntity<List<LibroDTO>> buscarPorEditorial(
        @Parameter(description = "Nombre de la editorial", required = true, example = "Planeta")
        @PathVariable String editorial
    ) {
        List<LibroDTO> libros = libroService.buscarPorEditorial(editorial);
        return libros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(libros);
    }

    @Operation(summary = "Eliminar libro del catálogo", description = "Elimina el registro del libro mediante su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente del catálogo"),
        @ApiResponse(responseCode = "404", description = "No se pudo eliminar: El libro no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(
        @Parameter(description = "ID del libro a eliminar", required = true, example = "2")
        @PathVariable Long id
    ) {
        boolean eliminado = libroService.eliminarLibro(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar datos de un libro", description = "Reescribe los campos de un libro identificándolo por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Información actualizada con éxito"),
        @ApiResponse(responseCode = "400", description = "Estructura JSON malformada o datos de validación erróneos"),
        @ApiResponse(responseCode = "404", description = "No se pudo actualizar: El ID proveído no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> actualizarLibro(
        @Parameter(description = "ID del libro a modificar", required = true, example = "1") @PathVariable Long id, 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura JSON con las modificaciones completas del libro", required = true)
        @Valid @RequestBody LibroCreateDTO dto
    ) {
        LibroDTO actualizado = libroService.actualizarLibro(id, dto);
        return ResponseEntity.ok(actualizado);
    }
}
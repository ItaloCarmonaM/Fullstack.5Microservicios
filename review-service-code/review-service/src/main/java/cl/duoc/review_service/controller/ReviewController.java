package cl.duoc.review_service.controller;

import cl.duoc.review_service.dto.ReviewCreateDTO;
import cl.duoc.review_service.dto.ReviewDTO;
import cl.duoc.review_service.service.ReviewService;
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

@Tag(name = "Reseñas", description = "Operaciones para gestionar valoraciones y comentarios de la plataforma")
@RestController
@RequestMapping("/api/v2/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Crear una nueva reseña", description = "Registra una nueva reseña.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Mal formato de los datos o fallas en las restricciones @Min/@Max"),
        @ApiResponse(responseCode = "404", description = "Error: El libro o el usuario no existen"),
        @ApiResponse(responseCode = "503", description = "Error de red: Uno de los microservicios remotos no responde")
    })
    @PostMapping
    public ResponseEntity<ReviewDTO> crearReview(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para componer la reseña", required = true)
        @Valid @RequestBody ReviewCreateDTO dto
    ) {
        return new ResponseEntity<>(reviewService.saveReview(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las reseñas", description = "Muestra todas las reseñas almacenadas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Listado completo obtenido")
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> obtenerTodasLasReviews() {
        return ResponseEntity.ok(reviewService.listaReviews());
    }

    @Operation(summary = "Buscar reseña por su ID", description = "Encuentra los detalles de una reseña usando su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
        @ApiResponse(responseCode = "404", description = "No existe ninguna reseña con el ID dado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> buscarPorId(
        @Parameter(description = "ID único de la reseña", required = true, example = "1")
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(reviewService.buscarPorId(id));
    }

    @Operation(summary = "Buscar reseñas por ID de Libro", description = "Lista todas las reseñas asociadas a un libro en específico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reseñas del libro obtenidas"),
        @ApiResponse(responseCode = "204", description = "El libro no tiene reseñas")
    })
    @GetMapping("/idLibro/{idLibro}")
    public ResponseEntity<List<ReviewDTO>> buscarPorIdLibro(
        @Parameter(description = "ID del libro a consultar", required = true, example = "10")
        @PathVariable Long idLibro
    ) {
        List<ReviewDTO> reviews = reviewService.buscarPorIdLibro(idLibro);
        return reviews.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Buscar reseñas por ID de Usuario", description = "Muestra todas las reseñas hechas por un usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reseñas del usuario obtenidas"),
        @ApiResponse(responseCode = "204", description = "El usuario no ha comentado ninguna reseña")
    })
    @GetMapping("/idUsuario/{idUsuario}")
    public ResponseEntity<List<ReviewDTO>> buscarPorIdUsuario(
        @Parameter(description = "ID del usuario a consultar", required = true, example = "5")
        @PathVariable Long idUsuario
    ) {
        List<ReviewDTO> reviews = reviewService.buscarPorIdUsuario(idUsuario);
        return reviews.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Buscar reseña por Libro y Usuario", description = "Muestra la reseña que un usuario le dedicó a un libro determinado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coincidencia encontrada"),
        @ApiResponse(responseCode = "204", description = "No se encontraron registros para esa combinación")
    })
    @GetMapping("/idLibroidUsuario")
    public ResponseEntity<List<ReviewDTO>> buscarPorIdLibroEIdUsuario(
        @Parameter(description = "ID del libro", required = true, example = "10") @RequestParam Long idLibro, 
        @Parameter(description = "ID del usuario", required = true, example = "5") @RequestParam Long idUsuario
    ) {
        List<ReviewDTO> reviews = reviewService.buscarPorIdLibroEIdUsuario(idLibro, idUsuario);
        return reviews.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Filtrar reseñas por calificación", description = "Muestra las reseñas según su puntuación asignada (1 al 5).")
    @GetMapping("/calificacion/{calificacion}")
    public ResponseEntity<List<ReviewDTO>> buscarPorCalificacion(
        @Parameter(description = "Puntuación", required = true, example = "5")
        @PathVariable Integer calificacion
    ) {
        List<ReviewDTO> reviews = reviewService.buscarPorCalificacion(calificacion);
        return reviews.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Eliminar una reseña", description = "Elimina una reseña del sistema.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reseña eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "No se pudo eliminar: La reseña no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReview(
        @Parameter(description = "ID de la reseña a eliminar", required = true, example = "1")
        @PathVariable Long id
    ) {
        return reviewService.eliminarReview(id) 
            ? ResponseEntity.noContent().build() 
            : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar una reseña existente", description = "Modifica el comentario o la calificación de una entrada identificada por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "No se encontró la reseña para actualizar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> actualizarReview(
        @Parameter(description = "ID de la reseña que se desea modificar", required = true, example = "1") @PathVariable Long id, 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos valores para el comentario y la calificación", required = true)
        @Valid @RequestBody ReviewCreateDTO dto
    ) {
        return ResponseEntity.ok(reviewService.actualizarReview(id, dto));
    }
}
package cl.duoc.review_service.controller;

import cl.duoc.review_service.dto.ReviewCreateDTO;
import cl.duoc.review_service.dto.ReviewDTO;
import cl.duoc.review_service.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> crearReview(@Valid @RequestBody ReviewCreateDTO dto) {
        return new ResponseEntity<>(reviewService.saveReview(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> obtenerTodasLasReviews() {
        return ResponseEntity.ok(reviewService.listaReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.buscarPorId(id));
    }

    @GetMapping("/idLibro/{idLibro}")
    public ResponseEntity<List<ReviewDTO>> buscarPorIdLibro(@PathVariable Long idLibro) {
        List<ReviewDTO> reviews = reviewService.buscarPorIdLibro(idLibro);
        return reviews.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reviews);
    }

    @GetMapping("/idUsuario/{idUsuario}")
    public ResponseEntity<List<ReviewDTO>> buscarPorIdUsuario(@PathVariable Long idUsuario) {
        List<ReviewDTO> reviews = reviewService.buscarPorIdUsuario(idUsuario);
        return reviews.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reviews);
    }

    @GetMapping("/idLibroidUsuario")
    public ResponseEntity<List<ReviewDTO>> buscarPorIdLibroEIdUsuario(
            @RequestParam Long idLibro, 
            @RequestParam Long idUsuario) {
        List<ReviewDTO> reviews = reviewService.buscarPorIdLibroEIdUsuario(idLibro, idUsuario);
        return reviews.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reviews);
    }

    @GetMapping("/calificacion/{calificacion}")
    public ResponseEntity<List<ReviewDTO>> buscarPorCalificacion(@PathVariable Integer calificacion) {
        List<ReviewDTO> reviews = reviewService.buscarPorCalificacion(calificacion);
        return reviews.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReview(@PathVariable Long id) {
        return reviewService.eliminarReview(id) 
            ? ResponseEntity.noContent().build() 
            : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> actualizarReview(@PathVariable Long id, @Valid @RequestBody ReviewCreateDTO dto) {
        return ResponseEntity.ok(reviewService.actualizarReview(id, dto));
    }
}
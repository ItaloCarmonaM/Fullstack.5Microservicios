package cl.duoc.review_service.controller;
import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.service.ReviewService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    
    @PostMapping
    public ResponseEntity<Review> crearReview(@Valid @RequestBody Review review) {
        Review nuevaReview = reviewService.saveReview(review);
        return new ResponseEntity<>(nuevaReview, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Review>> obtenerTodasLasReviews() {
        List<Review> reviews = reviewService.listaReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    // Buscar review por id
    @GetMapping("/{id}")
    public ResponseEntity<Review> buscarPorId(@PathVariable Long id) {
        Optional<Review> review = reviewService.buscarPorId(id);
        if(review.isPresent()) {
            return ResponseEntity.ok(review.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar review por idLibro
    @GetMapping("/idLibro/{idLibro}")
    public ResponseEntity<List<Review>> buscarPorIdLibro(@PathVariable Long idLibro) {
        List<Review> reviews = reviewService.buscarPorIdLibro(idLibro);
        if(!reviews.isEmpty()) {
            return ResponseEntity.ok(reviews);
        } else {    
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar review por idUsuario
    @GetMapping("/idUsuario/{idUsuario}")
    public ResponseEntity<List<Review>> buscarPorIdUsuario(@PathVariable Long idUsuario) {
        List<Review> reviews = reviewService.buscarPorIdUsuario(idUsuario);
        if(reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }

    // Buscar libro por idLibro e idUsuario, se usa la estructura: /api/v1/reviews/idLibroidUsuario?idLibro=1&idUsuario=2
    @GetMapping("/idLibroidUsuario")
    public ResponseEntity<List<Review>> buscarPorIdLibroEIdUsuario(@RequestParam Long idLibro, @RequestParam Long idUsuario) {
        List<Review> reviews = reviewService.buscarPorIdLibroEIdUsuario(idLibro, idUsuario);
        if(reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }

    // Buscar libro por calificacion
    @GetMapping("/calificacion/{calificacion}")
    public ResponseEntity<List<Review>> buscarPorCalificacion(@PathVariable Integer calificacion) {
        List<Review> reviews = reviewService.buscarPorCalificacion(calificacion);
        if(reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviews);
    }

    // Eliminar review por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReview(@PathVariable Long id) {
        boolean eliminado = reviewService.eliminarReview(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Actualizar review por id
    @PutMapping("/{id}")
    public ResponseEntity<Review> actualizarReview(@PathVariable Long id, @Valid @RequestBody Review reviewActualizada) {
        Review review = reviewService.actualizarReview(id, reviewActualizada);
        if (review != null) {
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.notFound().build();  
        }
    }
}
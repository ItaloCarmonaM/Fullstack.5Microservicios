package cl.duoc.review_service.repository;

import cl.duoc.review_service.model.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Pruebas de Integración con BD en Memoria - Capa de Repositorio")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("Debe persistir físicamente una reseña y recuperarla mediante su ID")
    void debeGuardarYBuscarReview() {
        // Given
        Review nuevaReview = new Review(null, 12L, 4L, "Muy interesante, recomendado.", 5);

        // When
        Review guardada = reviewRepository.save(nuevaReview);
        Optional<Review> encontrada = reviewRepository.findById(guardada.getId());

        // Then
        assertTrue(encontrada.isPresent());
        assertEquals("Muy interesante, recomendado.", encontrada.get().getComentario());
        assertEquals(5, encontrada.get().getCalificacion());
    }

    @Test
    @DisplayName("Debe filtrar y encontrar colecciones de reseñas por el ID del Libro")
    void debeBuscarPorIdLibro() {
        // Given
        Review rev1 = new Review(null, 50L, 1L, "Buena", 4);
        Review rev2 = new Review(null, 50L, 2L, "Regular", 3);
        Review rev3 = new Review(null, 99L, 3L, "Excelente", 5);

        reviewRepository.save(rev1);
        reviewRepository.save(rev2);
        reviewRepository.save(rev3);

        // When
        List<Review> resultados = reviewRepository.findByIdLibro(50L);

        // Then
        assertNotNull(resultados);
        assertEquals(2, resultados.size(), "Deberían encontrarse 2 reseñas para el libro con ID 50");
    }

    @Test
    @DisplayName("Debe encontrar registros que coincidan con la calificación filtrada")
    void debeBuscarPorCalificacion() {
        // Given
        Review r1 = new Review(null, 10L, 1L, "Malo", 1);
        Review r2 = new Review(null, 11L, 2L, "Pésimo", 1);

        reviewRepository.save(r1);
        reviewRepository.save(r2);

        // When
        List<Review> calificacionesUno = reviewRepository.findByCalificacion(1);

        // Then
        assertEquals(2, calificacionesUno.size());
    }
}
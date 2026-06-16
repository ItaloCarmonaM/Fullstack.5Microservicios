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
    @DisplayName("Debe probar save y findById")
    void debeProbarSaveYFindById() {
        Review nuevaReview = new Review(null, 12L, 4L, "Muy interesante", 5);
        Review guardada = reviewRepository.save(nuevaReview);
        
        Optional<Review> encontrada = reviewRepository.findById(guardada.getId());

        assertTrue(encontrada.isPresent());
        assertEquals("Muy interesante", encontrada.get().getComentario());
    }

    @Test
    @DisplayName("Debe probar findAll")
    void debeProbarFindAll() {
        Review rev = new Review(null, 50L, 1L, "Buena", 4);
        reviewRepository.save(rev);

        List<Review> resultados = reviewRepository.findAll();
        assertFalse(resultados.isEmpty());
    }
}
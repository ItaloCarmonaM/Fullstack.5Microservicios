package cl.duoc.review_service.service;

import cl.duoc.review_service.client.CatalogClient;
import cl.duoc.review_service.client.UserClient;
import cl.duoc.review_service.dto.LibroDTO;
import cl.duoc.review_service.dto.ReviewCreateDTO;
import cl.duoc.review_service.dto.ReviewDTO;
import cl.duoc.review_service.dto.UserDTO;
import cl.duoc.review_service.exception.RecursoNoEncontradoException;
import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - Capa de Servicio (ReviewService)")
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CatalogClient catalogClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("Debe crear una reseña")
    void debeCrearReview() {
        ReviewCreateDTO dto = new ReviewCreateDTO(10L, 5L, "Bueno", 4);
        Review review = new Review(1L, 10L, 5L, "Bueno", 4);
        
        // Mocks para saltar las validaciones de clientes externos del servicio
        when(catalogClient.getLibroById(10L)).thenReturn(new LibroDTO());
        when(userClient.getUserById(5L)).thenReturn(new UserDTO());
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        
        ReviewDTO resultado = reviewService.saveReview(dto);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Debe listar todas las reseñas")
    void debeListarReviews() {
        Review review = new Review(1L, 10L, 5L, "Bueno", 4);
        when(reviewRepository.findAll()).thenReturn(Collections.singletonList(review));

        List<ReviewDTO> resultado = reviewService.listaReviews();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe buscar una reseña por ID")
    void debeBuscarPorId() {
        Review review = new Review(1L, 10L, 5L, "Bueno", 4);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        ReviewDTO resultado = reviewService.buscarPorId(1L);
        assertNotNull(resultado);
        assertEquals("Bueno", resultado.getComentario());
    }

    @Test
    @DisplayName("Debe actualizar una reseña")
    void debeActualizarReview() {
        ReviewCreateDTO dto = new ReviewCreateDTO(10L, 5L, "Excelente", 5);
        Review reviewExistente = new Review(1L, 10L, 5L, "Bueno", 4);
        Review reviewActualizada = new Review(1L, 10L, 5L, "Excelente", 5);
        
        // Simular que encuentra la reseña original antes de editarla
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(reviewExistente));
        when(reviewRepository.save(any(Review.class))).thenReturn(reviewActualizada);

        ReviewDTO resultado = reviewService.actualizarReview(1L, dto);
        assertNotNull(resultado);
        assertEquals("Excelente", resultado.getComentario());
    }

    @Test
    @DisplayName("Debe eliminar una reseña")
    void debeEliminarReview() {
        when(reviewRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(reviewRepository).deleteById(1L);

        boolean eliminado = reviewService.eliminarReview(1L);
        assertTrue(eliminado);
    }

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si el ID no existe")
    void debeLanzarExcepcionCuandoIdNoExiste() {
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> {
            reviewService.buscarPorId(999L);
        });
    }
}
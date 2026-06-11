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
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

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
    @DisplayName("Debe guardar una reseña cuando el libro y el usuario existen")
    void debeGuardarResenaExitosamente() {
        // Given
        ReviewCreateDTO dto = new ReviewCreateDTO(10L, 5L, "Final muy inesperado", 5);
        
        LibroDTO mockLibro = new LibroDTO();
        mockLibro.setId(10L);
        mockLibro.setTitulo("El Psicoanalista");

        UserDTO mockUsuario = new UserDTO();
        mockUsuario.setId(5L);
        mockUsuario.setNombreCompleto("Juan Pérez");

        Review reviewGuardada = new Review(1L, 10L, 5L, "Final muy inesperado", 5);

        Mockito.when(catalogClient.getLibroById(10L)).thenReturn(mockLibro);
        Mockito.when(userClient.getUserById(5L)).thenReturn(mockUsuario);
        Mockito.when(reviewRepository.save(any(Review.class))).thenReturn(reviewGuardada);

        // When
        ReviewDTO resultado = reviewService.saveReview(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Final muy inesperado", resultado.getComentario());
        Mockito.verify(reviewRepository, Mockito.times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException cuando CatalogClient devuelve un error 404")
    void debeLanzarExcepcionCuandoLibroNoExiste() {
        // Given
        ReviewCreateDTO dto = new ReviewCreateDTO(99L, 5L, "Comentario", 4);
        FeignException.NotFound feignException = Mockito.mock(FeignException.NotFound.class);
        
        Mockito.when(catalogClient.getLibroById(99L)).thenThrow(feignException);

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            reviewService.saveReview(dto);
        });
        Mockito.verify(reviewRepository, Mockito.never()).save(any(Review.class));
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException al buscar por un ID de reseña inexistente")
    void debeLanzarExcepcionAlBuscarIdInexistente() {
        // Given
        Long idInexistente = 999L;
        Mockito.when(reviewRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            reviewService.buscarPorId(idInexistente);
        });
    }
}
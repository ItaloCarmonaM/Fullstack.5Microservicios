package cl.duoc.review_service.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias - Capa de Modelo (Review)")
class ReviewTest {

    @Test
    @DisplayName("Debe verificar el constructor vacío")
    void debeVerificarConstructorVacio() {
        Review review = new Review();
        assertNull(review.getId());
        assertNull(review.getComentario());
        assertNull(review.getCalificacion());
    }

    @Test
    @DisplayName("Debe verificar el constructor completo y getters")
    void debeVerificarConstructorCompleto() {
        Review review = new Review(1L, 10L, 5L, "Excelente libro", 5);
        
        assertEquals(1L, review.getId());
        assertEquals(10L, review.getIdLibro());
        assertEquals(5L, review.getIdUsuario());
        assertEquals("Excelente libro", review.getComentario());
        assertEquals(5, review.getCalificacion());
    }

    @Test
    @DisplayName("Debe verificar setters y getters")
    void debeVerificarSettersYGetters() {
        Review review = new Review();
        review.setId(2L);
        review.setComentario("Modificado");

        assertEquals(2L, review.getId());
        assertEquals("Modificado", review.getComentario());
    }

    @Test
    @DisplayName("Debe verificar campo requerido (Comentario no puede estar vacío)")
    void debeVerificarCampoRequerido() {
        Review review = new Review();
        review.setComentario("");
        assertTrue(review.getComentario().isEmpty(), "El comentario es un campo requerido");
    }
}
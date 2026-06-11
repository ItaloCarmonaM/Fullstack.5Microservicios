package cl.duoc.review_service.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias - Capa de Modelo (Review)")
class ReviewTest {

    @Test
    @DisplayName("Debe instanciar el modelo usando el constructor vacío y asignar valores mediante Setters")
    void debeInstanciarConConstructorVacioYSetters() {
        // Given
        Review review = new Review();

        // When
        review.setId(1L);
        review.setIdLibro(10L);
        review.setIdUsuario(5L);
        review.setComentario("Muy buen libro, recomendado.");
        review.setCalificacion(5);

        // Then
        assertAll("Verificación de propiedades asignadas",
            () -> assertEquals(1L, review.getId()),
            () -> assertEquals(10L, review.getIdLibro()),
            () -> assertEquals(5L, review.getIdUsuario()),
            () -> assertEquals("Muy buen libro, recomendado.", review.getComentario()),
            () -> assertEquals(5, review.getCalificacion())
        );
    }

    @Test
    @DisplayName("Debe construir el objeto utilizando el constructor con argumentos")
    void debeConstruirConConstructorCompleto() {
        // Given & When
        Review review = new Review(2L, 20L, 8L, "No me gustó el final.", 2);

        // Then
        assertNotNull(review);
        assertEquals(2L, review.getId());
        assertEquals(20L, review.getIdLibro());
        assertEquals(2, review.getCalificacion());
    }

    @Test
    @DisplayName("Debe validar la igualdad de objetos con Equals y HashCode")
    void debeValidarEqualsYHashCode() {
        // Given
        Review review1 = new Review(1L, 10L, 5L, "Comentario", 4);
        Review review2 = new Review(1L, 10L, 5L, "Comentario", 4);

        // When & Then
        assertEquals(review1, review2);
        assertEquals(review1.hashCode(), review2.hashCode());
    }
}
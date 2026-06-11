package cl.duoc.catalog_service.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias - Capa de Modelo (Libro)")
class LibroTest {

    @Test
    @DisplayName("Debe verificar el funcionamiento correcto de getters, setters y constructor completo")
    void debeVerificarSettersYGetters() {
        // Arrange
        Libro libro = new Libro();

        // Act
        libro.setId(1L);
        libro.setTitulo("Cien años de soledad");
        libro.setAutor("Gabriel García Márquez");
        libro.setCategoria("Realismo Mágico");
        libro.setEditorial("Sudamericana");
        libro.setTapa("Tapa Dura");
        libro.setPrecio(19990.0);

        // Assert
        assertAll("Verificación de atributos del Libro",
            () -> assertEquals(1L, libro.getId()),
            () -> assertEquals("Cien años de soledad", libro.getTitulo()),
            () -> assertEquals("Gabriel García Márquez", libro.getAutor()),
            () -> assertEquals("Realismo Mágico", libro.getCategoria()),
            () -> assertEquals("Sudamericana", libro.getEditorial()),
            () -> assertEquals("Tapa Dura", libro.getTapa()),
            () -> assertEquals(19990.0, libro.getPrecio())
        );
    }

    @Test
    @DisplayName("Debe validar que dos instancias con idénticos valores sean consideradas iguales por equals")
    void debeVerificarEqualsYHashCode() {
        // Arrange
        Libro libroA = new Libro(1L, "Subterra", "Baldomero Lillo", "Realismo", "Zig-Zag", "Tapa Blanda", 8990.0);
        Libro libroB = new Libro(1L, "Subterra", "Baldomero Lillo", "Realismo", "Zig-Zag", "Tapa Blanda", 8990.0);

        // Act & Assert
        assertEquals(libroA, libroB, "Instancias equivalentes estructuralmente deben pasar el criterio de igualdad");
        assertEquals(libroA.hashCode(), libroB.hashCode(), "Si dos objetos son iguales, sus HashCodes deben coincidir");
    }

    @Test
    @DisplayName("Debe verificar el constructor vacío y la inicialización nula por defecto")
    void debeVerificarConstructorVacio() {
        // Act
        Libro libroVacio = new Libro();

        // Assert
        assertNull(libroVacio.getId());
        assertNull(libroVacio.getTitulo());
        assertNull(libroVacio.getPrecio());
    }
}
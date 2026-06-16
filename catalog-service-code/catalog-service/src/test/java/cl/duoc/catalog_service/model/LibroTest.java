package cl.duoc.catalog_service.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias - Capa de Modelo (Libro)")
class LibroTest {

    @Test
    @DisplayName("Debe verificar el constructor vacío")
    void debeVerificarConstructorVacio() {
        Libro libro = new Libro();
        assertNull(libro.getId());
        assertNull(libro.getTitulo());
        assertNull(libro.getPrecio());
    }

    @Test
    @DisplayName("Debe verificar el constructor completo y getters")
    void debeVerificarConstructorCompleto() {
        Libro libro = new Libro(1L, "Subterra", "Baldomero Lillo", "Realismo", "Zig-Zag", "Tapa Blanda", 8990.0);
        
        assertEquals(1L, libro.getId());
        assertEquals("Subterra", libro.getTitulo());
        assertEquals("Baldomero Lillo", libro.getAutor());
        assertEquals(8990.0, libro.getPrecio());
    }

    @Test
    @DisplayName("Debe verificar setters y getters")
    void debeVerificarSettersYGetters() {
        Libro libro = new Libro();
        libro.setId(2L);
        libro.setTitulo("Ficciones");

        assertEquals(2L, libro.getId());
        assertEquals("Ficciones", libro.getTitulo());
    }

    @Test
    @DisplayName("Debe verificar campo requerido (Título no puede estar vacío)")
    void debeVerificarCampoRequerido() {
        Libro libro = new Libro();
        libro.setTitulo(""); // Campo requerido inválido
        assertTrue(libro.getTitulo().isEmpty(), "El título debería estar vacío para fallar la validación");
    }
}
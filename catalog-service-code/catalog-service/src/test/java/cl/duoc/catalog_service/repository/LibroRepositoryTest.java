package cl.duoc.catalog_service.repository;

import cl.duoc.catalog_service.model.Libro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("Pruebas de Integración con BD en Memoria - Capa de Repositorio (LibroRepository)")
class LibroRepositoryTest {

    @Autowired
    private LibroRepository libroRepository;

    @Test
    @DisplayName("Debe persistir un libro y permitir su búsqueda por título ignorando mayúsculas")
    void debeGuardarYBuscarPorTituloIgnoreCase() {
        // Arrange
        Libro libro = new Libro(null, "Estudio en Escarlata", "Arthur Conan Doyle", "Policial", "Orbis", "Tapa Dura", 19990.0);
        libroRepository.save(libro);

        // Act
        // Buscamos con un patrón alterado de mayúsculas
        List<Libro> encontrados = libroRepository.findByTituloIgnoreCase("eStUdiO eN eScArLaTa");

        // Assert
        assertFalse(encontrados.isEmpty(), "Debería localizar el registro ignorando el case de las letras");
        assertEquals(1, encontrados.size());
        assertEquals("Arthur Conan Doyle", encontrados.get(0).getAutor());
    }

    @Test
    @DisplayName("Debe retornar los registros cuyo costo sea estrictamente menor al parámetro provisto")
    void debeFiltrarPorPrecioMenorQue() {
        // Arrange
        Libro economico = new Libro(null, "Poemas", "Autor", "Poesía", "autor1", "Blanda", 4500.0);
        Libro caro = new Libro(null, "Enciclopedia", "Autor", "Educación", "autor2", "Dura", 45000.0);
        
        libroRepository.save(economico);
        libroRepository.save(caro);

        // Act
        List<Libro> librosBaratos = libroRepository.findByPrecioLessThan(10000.0);

        // Assert
        assertEquals(1, librosBaratos.size());
        assertEquals("Poemas", librosBaratos.get(0).getTitulo());
    }

    @Test
    @DisplayName("Debe encontrar una lista vacía si se filtra por una categoría inexistente en el catálogo")
    void debeRetornarVacioSiCategoriaNoExiste() {
        // Act
        List<Libro> resultado = libroRepository.findByCategoriaIgnoreCase("CategoriaAusente");

        // Assert
        assertTrue(resultado.isEmpty(), "La lista devuelta por JPA debe estar completamente vacía");
    }
}
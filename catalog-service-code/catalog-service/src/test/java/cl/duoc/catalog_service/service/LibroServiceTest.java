package cl.duoc.catalog_service.service;

import cl.duoc.catalog_service.dto.LibroCreateDTO;
import cl.duoc.catalog_service.dto.LibroDTO;
import cl.duoc.catalog_service.exceptions.LibroNotFoundException;
import cl.duoc.catalog_service.model.Libro;
import cl.duoc.catalog_service.repository.LibroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - Capa de Servicio (LibroService)")
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    @Test
    @DisplayName("Debe transformar un DTO y guardar un nuevo Libro en el catálogo")
    void debeRegistrarLibroExitosamente() {
        // Arrange
        LibroCreateDTO dto = new LibroCreateDTO("Ficciones", "Jorge Luis Borges", "Literatura", "Sur", "Tapa Blanda", 14990.0);
        Libro libroGuardado = new Libro(1L, "Ficciones", "Jorge Luis Borges", "Literatura", "Sur", "Tapa Blanda", 14990.0);

        Mockito.when(libroRepository.save(any(Libro.class))).thenReturn(libroGuardado);

        // Act
        LibroDTO resultado = libroService.registrarLibro(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ficciones", resultado.getTitulo());
        Mockito.verify(libroRepository, Mockito.times(1)).save(any(Libro.class));
    }

    @Test
    @DisplayName("Debe filtrar libros dentro de un margen económico mediante buscarPorPrecioEntre")
    void debeBuscarLibrosPorRangoDePrecios() {
        // Arrange
        Libro l1 = new Libro(1L, "Libro Barato", "Autor A", "Test", "Ed", "Tapa", 7000.0);
        Libro l2 = new Libro(2L, "Libro Medio", "Autor B", "Test", "Ed", "Tapa", 12000.0);
        Libro l3 = new Libro(3L, "Libro Caro", "Autor C", "Test", "Ed", "Tapa", 18000.0);

        Mockito.when(libroRepository.findByPrecioBetween(5000.0, 15000.0)).thenReturn(Arrays.asList(l1, l2));

        // Act
        List<LibroDTO> resultados = libroService.buscarPorPrecioEntre(5000.0, 15000.0);

        // Assert
        assertEquals(2, resultados.size());
        assertEquals("Libro Barato", resultados.get(0).getTitulo());
        assertEquals("Libro Medio", resultados.get(1).getTitulo());
    }

    @Test
    @DisplayName("Debe arrojar un LibroNotFoundException cuando se consulta un ID numérico que no existe")
    void debeLanzarExcepcionCuandoIdNoExiste() {
        // Arrange
        Long idInexistente = 99L;
        Mockito.when(libroRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LibroNotFoundException.class, () -> {
            libroService.buscarPorId(idInexistente);
        });
    }
}
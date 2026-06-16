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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - Capa de Servicio (LibroService)")
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    @Test
    @DisplayName("Debe crear/registrar un libro")
    void debaCrearLibro() {
        LibroCreateDTO dto = new LibroCreateDTO("Ficciones", "Jorge Luis Borges", "Literatura", "Sur", "Tapa Blanda", 14990.0);
        Libro libro = new Libro(1L, "Ficciones", "Jorge Luis Borges", "Literatura", "Sur", "Tapa Blanda", 14990.0);
        
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);
        
        LibroDTO resultado = libroService.registrarLibro(dto);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Debe listar todos los libros")
    void debeListarLibros() {
        Libro libro = new Libro(1L, "Ficciones", "Jorge Luis Borges", "Literatura", "Sur", "Tapa Blanda", 14990.0);
        when(libroRepository.findAll()).thenReturn(Collections.singletonList(libro));

        List<LibroDTO> resultado = libroService.listaLibros();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe buscar un libro por ID")
    void debeBuscarPorId() {
        Libro libro = new Libro(1L, "Ficciones", "Jorge Luis Borges", "Literatura", "Sur", "Tapa Blanda", 14990.0);
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        LibroDTO resultado = libroService.buscarPorId(1L);
        assertNotNull(resultado);
        assertEquals("Ficciones", resultado.getTitulo());
    }

    @Test
    @DisplayName("Debe actualizar un libro")
    void debeActualizarLibro() {
        LibroCreateDTO dto = new LibroCreateDTO("Ficciones Modificado", "Jorge Luis Borges", "Literatura", "Sur", "Tapa Blanda", 14990.0);
        Libro libro = new Libro(1L, "Ficciones Modificado", "Jorge Luis Borges", "Literatura", "Sur", "Tapa Blanda", 14990.0);
        
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);

        LibroDTO resultado = libroService.actualizarLibro(1L, dto);
        assertNotNull(resultado);
        assertEquals("Ficciones Modificado", resultado.getTitulo());
    }

    @Test
    @DisplayName("Debe eliminar un libro")
    void debeEliminarLibro() {
        when(libroRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(libroRepository).deleteById(1L);

        boolean eliminado = libroService.eliminarLibro(1L);
        assertTrue(eliminado);
    }

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si el ID no existe")
    void debeLanzarExcepcionCuandoIdNoExiste() {
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LibroNotFoundException.class, () -> {
            libroService.buscarPorId(99L);
        });
    }
}
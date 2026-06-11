package cl.duoc.catalog_service.controller;

import cl.duoc.catalog_service.dto.LibroCreateDTO;
import cl.duoc.catalog_service.dto.LibroDTO;
import cl.duoc.catalog_service.service.LibroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de Integración Web - Capa de Controlador (LibroController)")
class LibroControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LibroService libroService;

    @InjectMocks
    private LibroController libroController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(libroController).build();
    }

    @Test
    @DisplayName("POST -> Debe responder HTTP 201 Created y retornar el JSON del Libro registrado")
    void debeRetornar21AlRegistrarLibro() throws Exception {
        // Arrange
        LibroCreateDTO entrada = new LibroCreateDTO("El problema de los tres cuerpos", "Liu Cixin", "Ciencia Ficción", "Nova", "Tapa Blanda", 12500.0);
        LibroDTO salida = new LibroDTO(1L, "El problema de los tres cuerpos", "Liu Cixin", "Ciencia Ficción", "Nova", "Tapa Blanda", 12500.0);

        Mockito.when(libroService.registrarLibro(Mockito.any(LibroCreateDTO.class))).thenReturn(salida);

        // Act & Assert
        mockMvc.perform(post("/api/v2/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("El problema de los tres cuerpos"))
                .andExpect(jsonPath("$.precio").value(12500.0));
    }

    @Test
    @DisplayName("GET -> Debe retornar HTTP 200 OK con la lista completa de libros del catálogo")
    void debeRetornarListaDeLibrosOk() throws Exception {
        // Arrange
        LibroDTO libro = new LibroDTO(1L, "El Extranjero", "Cortázar", "Novela", "Lucemar", "Tapa Dura", 28990.0);
        List<LibroDTO> todosLosLibros = Collections.singletonList(libro);

        Mockito.when(libroService.listaLibros()).thenReturn(todosLosLibros);

        // Act & Assert 
        mockMvc.perform(get("/api/v2/libros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].titulo").value("El Extranjero"));
    }

    @Test
    @DisplayName("GET -> Debe retornar HTTP 400 Bad Request si el precio mínimo supera al máximo en el filtro entre")
    void debeRetornarBadRequestSiRangoPreciosEsInvalido() throws Exception {
        // Act & Assert
        // Solicitamos precioMin = 20000 y precioMax = 10000 (Rango invertido)
        mockMvc.perform(get("/api/v2/libros/precio/entre")
                .param("precioMin", "20000.0")
                .param("precioMax", "10000.0"))
                .andExpect(status().isBadRequest());
    }
}
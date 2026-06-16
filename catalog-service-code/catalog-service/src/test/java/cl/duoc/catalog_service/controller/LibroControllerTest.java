package cl.duoc.catalog_service.controller;

import cl.duoc.catalog_service.dto.LibroCreateDTO;
import cl.duoc.catalog_service.dto.LibroDTO;
import cl.duoc.catalog_service.exceptions.LibroNotFoundException;
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

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("POST -> Debe responder HTTP 201 Created")
    void debeRetornar201() throws Exception {
        LibroCreateDTO entrada = new LibroCreateDTO("Titulo", "Autor", "Categoria", "Editorial", "Tapa", 1000.0);
        LibroDTO salida = new LibroDTO(1L, "Titulo", "Autor", "Categoria", "Editorial", "Tapa", 1000.0);

        Mockito.when(libroService.registrarLibro(Mockito.any(LibroCreateDTO.class))).thenReturn(salida);

        mockMvc.perform(post("/api/v2/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET -> Debe retornar HTTP 200 OK")
    void debeRetornar200() throws Exception {
        Mockito.when(libroService.listaLibros()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/libros"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET -> Debe retornar HTTP 404 Not Found")
    void debeRetornar404() throws Exception {
        Mockito.when(libroService.buscarPorId(99L)).thenThrow(new LibroNotFoundException(99L));

        mockMvc.perform(get("/api/v2/libros/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET -> Debe retornar HTTP 400 Bad Request")
    void debeRetornar400() throws Exception {
        // Petición con parámetros inválidos de regla de negocio (Precio Min > Max)
        mockMvc.perform(get("/api/v2/libros/precio/entre")
                .param("precioMin", "20000.0")
                .param("precioMax", "10000.0"))
                .andExpect(status().isBadRequest());
    }
}
package cl.duoc.review_service.controller;

import cl.duoc.review_service.dto.ReviewCreateDTO;
import cl.duoc.review_service.dto.ReviewDTO;
import cl.duoc.review_service.exception.GlobalExceptionHandler; // Importamos tu manejador
import cl.duoc.review_service.exception.RecursoNoEncontradoException;
import cl.duoc.review_service.service.ReviewService;
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
@DisplayName("Pruebas de Integración - Capa de Controlador (ReviewController)")
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Registramos el GlobalExceptionHandler para que capture las excepciones y responda los códigos HTTP correctos
        this.mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST -> Debe responder HTTP 201 Created")
    void debeRetornar201() throws Exception {
        ReviewCreateDTO entrada = new ReviewCreateDTO(10L, 5L, "Muy buen libro", 4);
        ReviewDTO salida = new ReviewDTO(100L, 10L, 5L, "Muy buen libro", 4);

        Mockito.when(reviewService.saveReview(Mockito.any(ReviewCreateDTO.class))).thenReturn(salida);

        mockMvc.perform(post("/api/v2/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET -> Debe retornar HTTP 200 OK")
    void debeRetornar200() throws Exception {
        Mockito.when(reviewService.listaReviews()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/reviews"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET -> Debe retornar HTTP 404 Not Found")
    void debeRetornar404() throws Exception {
        // Corregido: .thenThrow en lugar de addThrows
        Mockito.when(reviewService.buscarPorId(999L))
               .thenThrow(new RecursoNoEncontradoException("No encontrado"));

        mockMvc.perform(get("/api/v2/reviews/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST -> Debe retornar HTTP 400 Bad Request ante payload inválido")
    void debeRetornar400() throws Exception {
        ReviewCreateDTO entradaInvalida = new ReviewCreateDTO();

        mockMvc.perform(post("/api/v2/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entradaInvalida)))
                .andExpect(status().isBadRequest());
    }
}
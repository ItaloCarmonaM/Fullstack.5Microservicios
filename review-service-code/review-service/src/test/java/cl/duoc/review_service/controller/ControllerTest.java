package cl.duoc.review_service.controller;

import cl.duoc.review_service.dto.ReviewCreateDTO;
import cl.duoc.review_service.dto.ReviewDTO;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    @DisplayName("POST /api/v2/reviews - Debe retornar 201 Created al registrar un payload válido")
    void debeRetornar201AlCrearReview() throws Exception {
        // Given
        ReviewCreateDTO dtoInput = new ReviewCreateDTO(10L, 5L, "Muy buen libro", 4);
        ReviewDTO dtoOutput = new ReviewDTO(100L, 10L, 5L, "Muy buen libro", 4);

        Mockito.when(reviewService.saveReview(anyValidDto())).thenReturn(dtoOutput);

        // When & Then
        mockMvc.perform(post("/api/v2/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.comentario").value("Muy buen libro"))
                .andExpect(jsonPath("$.calificacion").value(4));
    }

    @Test
    @DisplayName("GET /api/v2/reviews - Debe retornar 200 OK con la lista de reseñas en el sistema")
    void debeRetornar200AlObtenerTodas() throws Exception {
        // Given
        ReviewDTO review = new ReviewDTO(100L, 10L, 5L, "Comentario de prueba", 5);
        Mockito.when(reviewService.listaReviews()).thenReturn(List.of(review));

        // When & Then
        mockMvc.perform(get("/api/v2/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100L))
                .andExpect(jsonPath("$[0].comentario").value("Comentario de prueba"));
    }

    @Test
    @DisplayName("GET /api/v2/reviews/idLibro/{idLibro} - Debe retornar 204 No Content si el libro no registra reseñas")
    void debeRetornar204CuandoNoHayResenasParaElLibro() throws Exception {
        // Given
        Long idLibro = 40L;
        Mockito.when(reviewService.buscarPorIdLibro(idLibro)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/v2/reviews/idLibro/" + idLibro))
                .andExpect(status().isNoContent());
    }

    private ReviewCreateDTO anyValidDto() {
        return Mockito.any(ReviewCreateDTO.class);
    }
}
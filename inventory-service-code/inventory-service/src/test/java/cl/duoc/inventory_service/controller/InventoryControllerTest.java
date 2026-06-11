package cl.duoc.inventory_service.controller;

import cl.duoc.inventory_service.dto.InventoryCreateDTO;
import cl.duoc.inventory_service.dto.InventoryDTO;
import cl.duoc.inventory_service.service.InventoryService;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de Integración Web - Capa de Controlador (InventoryController)")
class InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    @DisplayName("POST -> Debe responder HTTP 201 Created al registrar un inventario válido")
    void debeRetornar201AlCrearInventario() throws Exception {
        // Arrange
        InventoryCreateDTO entrada = new InventoryCreateDTO(10L, 45);
        InventoryDTO salida = new InventoryDTO(1L, 10L, 45);
        Mockito.when(inventoryService.crearInventory(Mockito.any(InventoryCreateDTO.class))).thenReturn(salida);

        // Act & Assert
        mockMvc.perform(post("/api/v2/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stock").value(45));
    }

    @Test
    @DisplayName("GET -> Debe retornar HTTP 200 OK al consultar el stock por el ID del Libro")
    void debeRetornar200AlBuscarPorIdLibro() throws Exception {
        // Arrange
        Long idLibroBusqueda = 10L;
        InventoryDTO dtoSalida = new InventoryDTO(1L, idLibroBusqueda, 45);
        Mockito.when(inventoryService.buscarPorIdLibro(idLibroBusqueda)).thenReturn(dtoSalida);

        // Act & Assert
        mockMvc.perform(get("/api/v2/inventories/idLibro/{idLibro}", idLibroBusqueda))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(45));
    }

    @Test
    @DisplayName("DELETE -> Debe responder HTTP 204 No Content al eliminar un inventario existente")
    void debeRetornar204AlEliminarExitosamente() throws Exception {
        // Arrange
        Long idInventario = 1L;
        Mockito.when(inventoryService.eliminarInventory(idInventario)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v2/inventories/{id}", idInventario))
                .andExpect(status().isNoContent());
    }
}
package cl.duoc.inventory_service.service;

import cl.duoc.inventory_service.client.CatalogClient;
import cl.duoc.inventory_service.dto.InventoryCreateDTO;
import cl.duoc.inventory_service.dto.InventoryDTO;
import cl.duoc.inventory_service.dto.LibroDTO;
import cl.duoc.inventory_service.exception.RecursoNoEncontradoException;
import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.repository.InventoryRepository;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - Capa de Servicio (InventoryService)")
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    @DisplayName("Debe crear un inventario exitosamente tras validar el libro en Catalog")
    void debeCrearInventoryExitosamente() {
        // Arrange
        InventoryCreateDTO dto = new InventoryCreateDTO(10L, 45);
        
        LibroDTO libroSimulado = new LibroDTO();
        libroSimulado.setId(10L);
        libroSimulado.setTitulo("El Psicoanalista");

        Inventory inventarioGuardado = new Inventory(1L, 10L, 45);

        Mockito.when(catalogClient.getLibroById(10L)).thenReturn(libroSimulado);
        Mockito.when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventarioGuardado);

        // Act
        InventoryDTO resultado = inventoryService.crearInventory(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(10L, resultado.getIdLibro());
        assertEquals(45, resultado.getStock());
    }

    @Test
    @DisplayName("Debe lanzar RecursoNoEncontradoException si el cliente de Catálogo arroja un error 404")
    void debeLanzarExcepcionCuandoLibroNoExisteEnCatalog() {
        // Arrange
        InventoryCreateDTO dto = new InventoryCreateDTO(99L, 10);
        
        FeignException.NotFound mockNotFound = Mockito.mock(FeignException.NotFound.class);
        Mockito.when(catalogClient.getLibroById(99L)).thenThrow(mockNotFound);

        // Act & Assert
        assertThrows(RecursoNoEncontradoException.class, () -> {
            inventoryService.crearInventory(dto);
        });
    }

    @Test
    @DisplayName("Debe actualizar el stock de un registro de inventario existente")
    void debeActualizarStockExitosamente() {
        // Arrange
        Long idInventario = 1L;
        InventoryCreateDTO dto = new InventoryCreateDTO(10L, 100);
        Inventory inventarioExistente = new Inventory(1L, 10L, 45);
        Inventory inventarioActualizado = new Inventory(1L, 10L, 100);

        Mockito.when(inventoryRepository.findById(idInventario)).thenReturn(Optional.of(inventarioExistente));
        Mockito.when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventarioActualizado);

        // Act
        InventoryDTO resultado = inventoryService.actualizarStock(idInventario, dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(100, resultado.getStock());
    }
}
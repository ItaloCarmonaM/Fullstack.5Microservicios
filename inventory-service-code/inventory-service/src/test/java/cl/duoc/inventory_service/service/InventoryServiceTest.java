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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        // Given
        InventoryCreateDTO dto = new InventoryCreateDTO(10L, 45);
        LibroDTO libroSimulado = new LibroDTO();
        libroSimulado.setId(10L);
        Inventory inventarioGuardado = new Inventory(1L, 10L, 45);

        when(catalogClient.getLibroById(10L)).thenReturn(libroSimulado);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventarioGuardado);

        // When
        InventoryDTO resultado = inventoryService.crearInventory(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(45, resultado.getStock());
    }

    @Test
    @DisplayName("Debe listar todos los inventarios existentes")
    void debeListarInventarios() {
        // Given
        Inventory inventario = new Inventory(1L, 10L, 45);
        when(inventoryRepository.findAll()).thenReturn(List.of(inventario));

        // When
        List<InventoryDTO> lista = inventoryService.listarInventories();

        // Then
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    @DisplayName("Debe buscar un inventario por su ID")
    void debeBuscarPorId() {
        // Given
        Inventory inventario = new Inventory(1L, 10L, 45);
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventario));

        // When
        InventoryDTO encontrado = inventoryService.buscarPorId(1L);

        // Then
        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
    }

    @Test
    @DisplayName("Debe actualizar el stock de un registro de inventario existente")
    void debeActualizarStockExitosamente() {
        // Given
        Long idInventario = 1L;
        InventoryCreateDTO dto = new InventoryCreateDTO(10L, 100);
        Inventory inventarioExistente = new Inventory(1L, 10L, 45);
        Inventory inventarioActualizado = new Inventory(1L, 10L, 100);

        when(inventoryRepository.findById(idInventario)).thenReturn(Optional.of(inventarioExistente));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventarioActualizado);

        // When
        InventoryDTO resultado = inventoryService.actualizarStock(idInventario, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(100, resultado.getStock());
    }

    @Test
    @DisplayName("Debe eliminar un registro de inventario si este existe")
    void debeEliminarInventoryExitosamente() {
        // Given
        when(inventoryRepository.existsById(1L)).thenReturn(true);

        // When
        boolean eliminado = inventoryService.eliminarInventory(1L);

        // Then
        assertTrue(eliminado);
    }

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar RecursoNoEncontradoException si Catalog arroja 404")
    void debeLanzarExcepcionCuandoLibroNoExisteEnCatalog() {
        // Given
        InventoryCreateDTO dto = new InventoryCreateDTO(99L, 10);
        FeignException.NotFound mockNotFound = Mockito.mock(FeignException.NotFound.class);
        when(catalogClient.getLibroById(99L)).thenThrow(mockNotFound);

        // When & Then
        assertThrows(RecursoNoEncontradoException.class, () -> {
            inventoryService.crearInventory(dto);
        });
    }
}
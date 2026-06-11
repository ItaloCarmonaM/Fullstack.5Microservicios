package cl.duoc.inventory_service.repository;

import cl.duoc.inventory_service.model.Inventory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("Pruebas de Integración con BD - Capa de Repositorio (InventoryRepository)")
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    @DisplayName("Debe persistir un inventario y recuperarlo usando findByIdLibro")
    void debeGuardarYBuscarPorIdLibro() {
        // Arrange
        Inventory inventario = new Inventory(null, 500L, 120);
        inventoryRepository.save(inventario);

        // Act
        Inventory encontrado = inventoryRepository.findByIdLibro(500L);

        // Assert
        assertNotNull(encontrado);
        assertEquals(120, encontrado.getStock());
    }

    @Test
    @DisplayName("Debe retornar null si se busca un ID de libro que no tiene stock asignado")
    void debeRetornarNullSiIdLibroNoExiste() {
        // Act
        Inventory resultado = inventoryRepository.findByIdLibro(9999L);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Debe eliminar un registro de inventario de forma efectiva")
    void debeEliminarInventarioCorrectamente() {
        // Arrange
        Inventory inventario = inventoryRepository.save(new Inventory(null, 300L, 15));
        Long idGenerado = inventario.getId();

        // Act
        inventoryRepository.deleteById(idGenerado);
        boolean existe = inventoryRepository.existsById(idGenerado);

        // Assert
        assertFalse(existe);
    }
}
package cl.duoc.inventory_service.repository;

import cl.duoc.inventory_service.model.Inventory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("Pruebas de Integración con BD - Capa de Repositorio (InventoryRepository)")
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    @DisplayName("Debe ejecutar la operación save() y persistir los datos de inventario")
    void debeGuardarInventario() {
        // Arrange
        Inventory inventario = new Inventory(null, 500L, 120);

        // Act
        Inventory guardado = inventoryRepository.save(inventario);

        // Assert
        assertNotNull(guardado.getId());
        assertEquals(500L, guardado.getIdLibro());
    }

    @Test
    @DisplayName("Debe ejecutar la operación findById() y recuperar el registro correspondiente")
    void debeBuscarPorId() {
        // Arrange
        Inventory guardado = inventoryRepository.save(new Inventory(null, 600L, 30));

        // Act
        Optional<Inventory> encontrado = inventoryRepository.findById(guardado.getId());

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals(30, encontrado.get().getStock());
    }

    @Test
    @DisplayName("Debe ejecutar la operación findAll() y retornar la lista completa de registros")
    void debeBuscarTodos() {
        // Arrange
        inventoryRepository.save(new Inventory(null, 700L, 10));
        inventoryRepository.save(new Inventory(null, 800L, 20));

        // Act
        List<Inventory> lista = inventoryRepository.findAll();

        // Assert
        assertTrue(lista.size() >= 2);
    }
}
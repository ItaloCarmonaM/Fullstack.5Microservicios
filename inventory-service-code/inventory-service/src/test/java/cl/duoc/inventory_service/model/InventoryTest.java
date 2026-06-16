package cl.duoc.inventory_service.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias - Capa de Modelo (Inventory)")
class InventoryTest {

    @Test
    @DisplayName("Debe verificar el funcionamiento de getters, setters y constructor")
    void debeVerificarSettersYGetters() {
        // Arrange
        Inventory inventario = new Inventory();

        // Act
        inventario.setId(1L);
        inventario.setIdLibro(101L);
        inventario.setStock(50);

        // Assert
        assertAll("Verificación de atributos del Inventario",
            () -> assertEquals(1L, inventario.getId()),
            () -> assertEquals(101L, inventario.getIdLibro()),
            () -> assertEquals(50, inventario.getStock())
        );
    }

    @Test
    @DisplayName("Debe validar que dos instancias con idénticos valores sean consideradas iguales por equals")
    void debeVerificarEqualsYHashCode() {
        // Arrange
        Inventory invA = new Inventory(1L, 202L, 10);
        Inventory invB = new Inventory(1L, 202L, 10);

        // Act & Assert
        assertEquals(invA, invB);
        assertEquals(invA.hashCode(), invB.hashCode());
    }

    @Test
    @DisplayName("Debe verificar el constructor vacío y la inicialización nula por defecto")
    void debeVerificarConstructorVacio() {
        // Act
        Inventory invVacio = new Inventory();

        // Assert
        assertNull(invVacio.getId());
        assertNull(invVacio.getIdLibro());
        assertNull(invVacio.getStock());
    }
}
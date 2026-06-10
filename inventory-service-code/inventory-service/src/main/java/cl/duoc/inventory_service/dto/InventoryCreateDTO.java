package cl.duoc.inventory_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
@Schema(description = "Estructura de datos requerida para crear o actualizar un inventario")
public class InventoryCreateDTO {

    @Schema(
        description = "ID único del libro proveniente del microservicio de Catalog", 
        example = "10", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "El ID del libro no puede ser nulo")
    private Long idLibro;

    @Schema(
        description = "Cantidad física de libros disponibles en bodega", 
        example = "45", 
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}
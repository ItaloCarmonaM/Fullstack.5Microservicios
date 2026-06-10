package cl.duoc.inventory_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo de respuesta que representa la información del inventario almacenado")
public class InventoryDTO {

    @Schema(description = "ID auto-incremental único del inventario", example = "1")
    private Long id;

    @Schema(description = "ID del libro asociado", example = "10")
    private Long idLibro;

    @Schema(description = "Cantidad actual de unidades en stock", example = "45")
    private Integer stock;
}
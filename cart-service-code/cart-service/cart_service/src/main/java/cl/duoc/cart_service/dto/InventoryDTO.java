package cl.duoc.cart_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Información de Stock")
public class InventoryDTO {
    @Schema(description = "ID del inventario", example = "105")
    private Long id;
    @Schema(description = "ID del libro evaluado", example = "42")
    private Long idLibro;
    @Schema(description = "Unidades disponibles en almacén", example = "50")
    private Integer stock;
}
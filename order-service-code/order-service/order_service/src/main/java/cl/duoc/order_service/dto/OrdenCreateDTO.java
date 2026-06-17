package cl.duoc.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo de datos requerido para registrar una nueva orden de compra")
public class OrdenCreateDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID único del usuario que realiza la compra", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idUsuario;

    @NotNull(message = "El total de la compra es obligatorio")
    @Positive(message = "El total debe ser un monto positivo")
    @Schema(description = "Monto enviado (El backend lo recalculará sincrónicamente mediante el Carrito)", example = "24990.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double total;
}
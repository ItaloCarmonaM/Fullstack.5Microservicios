package cl.duoc.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo de respuesta que representa los datos de una orden")
public class OrdenDTO {

    @Schema(description = "ID generado automáticamente de la orden", example = "1")
    private Long id;

    @Schema(description = "ID del usuario", example = "45")
    private Long idUsuario;

    @Schema(description = "Fecha y hora en la que se generó la orden", example = "2026-06-16T15:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Monto final de la compra", example = "24990.50")
    private Double total;

    @Schema(description = "Estado actual de la orden", example = "PROCESADA")
    private String estado;
}
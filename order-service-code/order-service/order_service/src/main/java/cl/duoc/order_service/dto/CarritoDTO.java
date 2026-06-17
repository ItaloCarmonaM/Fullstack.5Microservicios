package cl.duoc.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura de un carrito")
public class CarritoDTO {
    @Schema(description = "ID del carrito", example = "12")
    private Long id;
    @Schema(description = "ID del dueño del carrito", example = "5")
    private Long idUsuario;
    @Schema(description = "ID del libro añadido", example = "301")
    private Long idLibro;
    @Schema(description = "Cantidad solicitada", example = "2")
    private Integer cantidad;
    @Schema(description = "Título del libro", example = "Cien años de soledad")
    private String tituloLibro;
    @Schema(description = "Precio asignado al libro", example = "12495.0")
    private Double precioUnitario;
    @Schema(description = "Subtotal acumulado (precio * cantidad)", example = "24990.0")
    private Double subtotal;
}
package cl.duoc.cart_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representación de un ítem dentro del carrito")
public class CarritoDTO {

    @Schema(description = "ID del registro del ítem en el carrito", example = "10")
    private Long id;

    @Schema(description = "ID del usuario dueño del carrito", example = "1")
    private Long idUsuario;

    @Schema(description = "ID del libro guardado", example = "42")
    private Long idLibro;

    @Schema(description = "Cantidad de unidades seleccionadas", example = "2")
    private Integer cantidad;

    @Schema(description = "Título del libro", example = "Cien años de soledad")
    private String tituloLibro;

    @Schema(description = "Precio del libro", example = "15990.0")
    private Double precioUnitario;

    @Schema(description = "Subtotal calculado para este ítem (cantidad * precio)", example = "31980.0")
    private Double subtotal;
}
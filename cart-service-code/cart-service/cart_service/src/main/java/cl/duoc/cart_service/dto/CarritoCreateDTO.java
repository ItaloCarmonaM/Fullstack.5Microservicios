package cl.duoc.cart_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos requeridos para agregar un libro en el carrito")
public class CarritoCreateDTO {

    @NotNull(message = "El ID del usuario no puede ser nulo")
    @Schema(description = "ID del usuario dueño del carrito", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idUsuario;

    @NotNull(message = "El ID del libro no puede ser nulo")
    @Schema(description = "ID del libro que se desea añadir", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idLibro;

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    @Schema(description = "Cantidad de unidades del libro a comprar", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;
}
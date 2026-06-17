package cl.duoc.cart_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Información y precio de un libro")
public class LibroDTO {
    @Schema(description = "ID del libro", example = "42")
    private Long id;
    @Schema(description = "Título del libro", example = "Cien años de soledad")
    private String titulo;
    @Schema(description = "Precio del libro", example = "15990.0")
    private Double precio;
}
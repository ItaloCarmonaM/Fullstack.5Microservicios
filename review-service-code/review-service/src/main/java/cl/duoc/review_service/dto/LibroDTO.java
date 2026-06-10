package cl.duoc.review_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos de referencia del libro")
public class LibroDTO {
    @Schema(example = "10") private Long id;
    @Schema(example = "El Psicoanalista") private String titulo;
    @Schema(example = "John Katzenbach") private String autor;
}
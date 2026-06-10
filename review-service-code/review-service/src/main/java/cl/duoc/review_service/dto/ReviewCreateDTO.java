package cl.duoc.review_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura requerida para registrar o modificar una reseña")
public class ReviewCreateDTO {

    @Schema(description = "ID del libro a calificar", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del libro no puede ser nulo")
    private Long idLibro;

    @Schema(description = "ID del usuario que escribe la reseña", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long idUsuario;

    @Schema(description = "Opinión o comentario sobre la lectura", example = "Excelente narrativa y giros argumentales inesperados.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El comentario no puede estar vacío")
    private String comentario;

    @Schema(description = "Nota asignada al libro (Escala del 1 al 5)", example = "5", minimum = "1", maximum = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @NotNull(message = "La calificación no puede ser nula")
    private Integer calificacion;
}
package cl.duoc.review_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo de salida con los detalles de una reseña")
public class ReviewDTO {

    @Schema(description = "ID único de la reseña", example = "1")
    private Long id;

    @Schema(description = "ID del libro asociado", example = "10")
    private Long idLibro;

    @Schema(description = "ID del usuario autor de la reseña", example = "5")
    private Long idUsuario;

    @Schema(description = "Cuerpo de la reseña", example = "Excelente narrativa y giros argumentales inesperados.")
    private String comentario;

    @Schema(description = "Calificación numérica otorgada", example = "5")
    private Integer calificacion;
}
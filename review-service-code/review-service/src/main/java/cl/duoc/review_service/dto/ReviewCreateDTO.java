package cl.duoc.review_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDTO {
    @NotNull(message = "El ID del libro no puede ser nulo")
    private Long idLibro;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long idUsuario;

    @NotBlank(message = "El comentario no puede estar vacío")
    private String comentario;

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @NotNull(message = "La calificación no puede ser nula")
    private Integer calificacion;
}
package cl.duoc.review_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long idLibro;
    private Long idUsuario;
    private String comentario;
    private Integer calificacion;
}
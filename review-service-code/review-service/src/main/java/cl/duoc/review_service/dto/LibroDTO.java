package cl.duoc.review_service.dto;
import lombok.Data;

@Data
public class LibroDTO { // Copia exacta/espejo de lo que devuelve Catalog
    private Long id;
    private String titulo;
    private String autor;
}
package cl.duoc.catalog_service.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTO {
    private Long id;
    private String titulo;
    private String autor;
    private String categoria;
    private String editorial;
    private String tapa;
    private Double precio;
    
}

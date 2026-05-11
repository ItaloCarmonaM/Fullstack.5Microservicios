package cl.duoc.catalog_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroCreateDTO {
    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacío")
    private String autor;

    @NotBlank(message= "La categoría no puede estar vacía")
    private String categoria;

    @NotBlank(message= "La editorial no puede estar vacía")
    private String editorial;

    @NotBlank(message= "La tapa no puede estar vacía")
    private String tapa;
    
    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;
}

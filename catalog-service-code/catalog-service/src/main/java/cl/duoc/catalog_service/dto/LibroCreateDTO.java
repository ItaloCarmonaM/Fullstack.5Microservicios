package cl.duoc.catalog_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura de datos requerida para registrar o actualizar un libro en el catálogo")
public class LibroCreateDTO {

    @Schema(description = "Título del libro", example = "Cien años de soledad", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @Schema(description = "Nombre completo del autor", example = "Gabriel García Márquez", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El autor no puede estar vacío")
    private String autor;

    @Schema(description = "Categoría de clasificación del libro", example = "Realismo Mágico", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La categoría no puede estar vacía")
    private String categoria;

    @Schema(description = "Editorial encargada de la publicación de la obra.", example = "Editorial Sudamericana", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La editorial no puede estar vacía")
    private String editorial;

    @Schema(description = "Encuadernación de la edición del libro", example = "Tapa Dura", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La tapa no puede estar vacía")
    private String tapa;
    
    @Schema(description = "Precio de venta", example = "19990.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;
}
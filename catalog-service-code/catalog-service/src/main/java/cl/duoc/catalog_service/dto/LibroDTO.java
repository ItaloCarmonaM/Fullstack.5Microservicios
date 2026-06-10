package cl.duoc.catalog_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo de salida con los detalles informativos de un libro")
public class LibroDTO {

    @Schema(description = "ID auto-incremental único asignado por la base de datos", example = "1")
    private Long id;

    @Schema(description = "Título del libro", example = "Cien años de soledad")
    private String titulo;

    @Schema(description = "Autor del libro", example = "Gabriel García Márquez")
    private String autor;

    @Schema(description = "Categoría del libro", example = "Realismo Mágico")
    private String categoria;

    @Schema(description = "Editorial del libro", example = "Editorial Sudamericana")
    private String editorial;

    @Schema(description = "Encuadernación de la edición del libro", example = "Tapa Dura")
    private String tapa;

    @Schema(description = "Precio de venta", example = "19990.0")
    private Double precio;
}
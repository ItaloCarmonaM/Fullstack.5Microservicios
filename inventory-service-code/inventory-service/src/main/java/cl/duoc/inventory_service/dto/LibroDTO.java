package cl.duoc.inventory_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Modelo que representa los datos del libro obtenidos desde el servicio de Catalog")
public class LibroDTO {

    @Schema(description = "Identificador único del libro", example = "10")
    private Long id;

    @Schema(description = "Título del libro", example = "El Psicoanalista")
    private String titulo;

    @Schema(description = "Autor del libro", example = "John Katzenbach")
    private String autor;

    @Schema(description = "Editorial encargada de la publicación", example = "B de Bolsillo")
    private String editorial;

    @Schema(description = "Encuadernación de la edición", example = "Tapa Blanda")
    private String tapa;
}
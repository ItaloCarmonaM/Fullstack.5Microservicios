package cl.duoc.inventory_service.dto;

import lombok.Data;

@Data
public class LibroDTO {
    private Long id;
    private String titulo;
    private String autor;
    private String editorial;
    private String tapa;
}
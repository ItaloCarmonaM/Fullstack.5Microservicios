package cl.duoc.inventory_service.dto;

import lombok.Data;

@Data
public class LibroDTO { // Este es el espejo del que está en Catalog
    private Long id;
    private String titulo;
    private String autor;
    private String editorial;
    private String tapa;
}
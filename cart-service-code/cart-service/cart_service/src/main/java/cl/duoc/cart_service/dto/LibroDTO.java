package cl.duoc.cart_service.dto;

import lombok.Data;

@Data
public class LibroDTO {
    private Long id;
    private String titulo;
    private Double precio;
}
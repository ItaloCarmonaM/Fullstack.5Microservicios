package cl.duoc.cart_service.dto;

import lombok.Data;

@Data
public class InventoryDTO {
    private Long id;
    private Long idLibro;
    private Integer stock;
}
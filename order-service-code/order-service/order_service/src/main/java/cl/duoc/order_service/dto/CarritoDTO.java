package cl.duoc.order_service.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoDTO {
    private Long id;
    private Long idUsuario;
    private Long idLibro;
    private Integer cantidad;
    
    private String tituloLibro;
    private Double precioUnitario;
    private Double subtotal;
}
package cl.duoc.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDTO {
    private Long id;
    private Long idUsuario;
    private Long idLibro;
    private Integer cantidad;
}
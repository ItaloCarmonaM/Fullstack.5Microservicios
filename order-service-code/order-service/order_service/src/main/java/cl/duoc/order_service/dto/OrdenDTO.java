package cl.duoc.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDTO {
    private Long id;
    private Long idUsuario;
    private LocalDateTime fechaCreacion;
    private Double total;
    private String estado;
}
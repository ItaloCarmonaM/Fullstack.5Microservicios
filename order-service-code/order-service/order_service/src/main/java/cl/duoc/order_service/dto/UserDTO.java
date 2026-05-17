package cl.duoc.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private Long idAuth;
    private String nombreCompleto;
    private String rut;
    private String fechaNacimiento;
    private String numeroTelefono;
    private String direccion;
}
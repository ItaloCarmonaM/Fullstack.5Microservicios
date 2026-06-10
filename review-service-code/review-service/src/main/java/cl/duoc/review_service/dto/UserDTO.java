package cl.duoc.review_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de referencia del usuario recuperados desde la EC2 externa")
public class UserDTO {
    @Schema(example = "5") private Long id;
    @Schema(example = "101") private Long idAuth;
    @Schema(example = "Juan Pérez") private String nombreCompleto;
    @Schema(example = "12345678-9") private String rut;
    @Schema(example = "1990-05-15") private String fechaNacimiento;
    @Schema(example = "+56912345678") private String numeroTelefono;
    @Schema(example = "Av. Siempre Viva 742, Puente Alto") private String direccion;
}
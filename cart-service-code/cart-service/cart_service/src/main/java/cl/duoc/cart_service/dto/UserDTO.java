package cl.duoc.cart_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos del Usuario ")
public class UserDTO {
    @Schema(description = "ID del usuario", example = "1")
    private Long id;
    @Schema(description = "ID de la cuenta de autenticación asociada", example = "1001")
    private Long idAuth;
    @Schema(description = "Nombre completo de la cuenta", example = "Juan Pérez")
    private String nombreCompleto;
    @Schema(description = "RUT del usuario", example = "12.345.678-9")
    private String rut;
    @Schema(description = "Fecha de nacimiento del usuario", example = "1990-05-15")
    private String fechaNacimiento;
    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String numeroTelefono;
    @Schema(description = "Dirección de despacho", example = "Av. Concha y Toro 1340, Puente Alto")
    private String direccion;
}
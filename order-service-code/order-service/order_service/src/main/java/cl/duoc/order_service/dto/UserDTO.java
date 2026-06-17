package cl.duoc.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información del usuario")
public class UserDTO {
    
    @Schema(description = "ID del usuario", example = "45")
    private Long id;
    
    @Schema(description = "ID del sistema de autenticación", example = "1002")
    private Long idAuth;
    
    @Schema(description = "Nombre del comprador", example = "Juan Carlos Bodoque")
    private String nombreCompleto;
    
    @Schema(description = "RUT del usuario", example = "12345678-9")
    private String rut;
    
    @Schema(description = "Fecha de nacimiento", example = "1990-03-15")
    private String fechaNacimiento;
    
    @Schema(description = "Número de teléfono", example = "+56912345678")
    private String numeroTelefono;
    
    @Schema(description = "Dirección de despacho", example = "Av. Siempre Viva 742, Santiago")
    private String direccion;
}
package cl.duoc.order_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long idUsuario;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDateTime fechaCreacion;

    @NotNull(message = "El monto total no puede ser nulo")
    @Positive(message = "El monto total debe ser mayor a 0")
    private Double total;

    @NotNull(message = "El estado no puede estar vacío")
    private String estado; // Ejemplo: PENDIENTE, PAGADA, ENVIADA, CANCELADA
}
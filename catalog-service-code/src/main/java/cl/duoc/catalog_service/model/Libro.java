package cl.duoc.catalog_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacío")
    private String autor;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;
}
package cl.duoc.review_service.model;

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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del libro no puede ser nulo")
    private Long idLibro; // ID del libro que viene del CatalogService

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long idUsuario; // ID del usuario que viene del UserService

    @NotBlank(message = "El comentario no puede estar vacío")
    private String comentario;

    @Min(value = 1, message = "La calificación debe ser un número entre 1 y 5")
    @Max(value = 5, message = "La calificación debe ser un número entre 1 y 5")
    @NotNull(message = "La calificación no puede ser nula")
    private Integer calificacion; // Calificación de 1 a 5 estrellas
}

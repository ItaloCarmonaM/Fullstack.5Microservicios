package cl.duoc.review_service.service;

import cl.duoc.review_service.client.CatalogClient;
import cl.duoc.review_service.client.UserClient; // Importamos el nuevo cliente
import cl.duoc.review_service.dto.ReviewCreateDTO;
import cl.duoc.review_service.dto.ReviewDTO;
import cl.duoc.review_service.dto.LibroDTO;
import cl.duoc.review_service.dto.UserDTO; // Importamos el DTO de usuario
import cl.duoc.review_service.exception.RecursoNoEncontradoException;
import cl.duoc.review_service.exception.ServicioNoDisponibleException;
import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.repository.ReviewRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CatalogClient catalogClient; 

    @Autowired
    private UserClient userClient; // Inyección de dependencia del servicio externo

    public ReviewDTO saveReview(ReviewCreateDTO dto) {
        // 1. VALIDACIÓN DEL LIBRO (Catálogo Local)
        log.info("Solicitando validación de existencia para libro ID={} al servicio Catalog", dto.getIdLibro());
        LibroDTO libro;
        try {
            libro = catalogClient.getLibroById(dto.getIdLibro());
            log.info("Libro confirmado correctamente: ID={}, Título='{}'", libro.getId(), libro.getTitulo());
        } catch (FeignException.NotFound e) {
            log.warn("Servicio Catalog respondió 404: El libro ID={} no existe", dto.getIdLibro());
            throw new RecursoNoEncontradoException("No se puede guardar la reseña: El libro especificado no existe.");
        } catch (FeignException e) {
            log.error("Error de comunicación con el servicio Catalog: {}", e.getMessage());
            throw new ServicioNoDisponibleException("El servicio de Catálogo no se encuentra disponible temporalmente.");
        }

        // 2. VALIDACIÓN DEL USUARIO (Servicio Externo en otra EC2)
        log.info("Solicitando validación de existencia para usuario ID={} al servicio externo de Usuarios", dto.getIdUsuario());
        UserDTO usuario;
        try {
            usuario = userClient.getUserById(dto.getIdUsuario());
            log.info("Usuario verificado exitosamente en la otra EC2: ID={}, Nombre='{}'", usuario.getId(), usuario.getNombreCompleto());
        } catch (FeignException.NotFound e) {
            log.warn("Servicio de Usuarios respondió 404: El usuario ID={} no existe en el sistema", dto.getIdUsuario());
            throw new RecursoNoEncontradoException("No se puede guardar la reseña: El usuario especificado no existe en los registros.");
        } catch (FeignException e) {
            log.error("Falla crítica en la comunicación de red con la EC2 externa de Usuarios: {}", e.getMessage());
            throw new ServicioNoDisponibleException("El servicio de verificación de usuarios no responde. Intente más tarde.");
        }

        // 3. GENERAR Y PERSISTIR LA RESEÑA
        Review review = new Review();
        review.setIdLibro(libro.getId()); 
        review.setIdUsuario(usuario.getId()); // Usamos el ID validado por el servicio de usuarios
        review.setComentario(dto.getComentario());
        review.setCalificacion(dto.getCalificacion());

        Review guardada = reviewRepository.save(review);
        log.info("Reseña ID={} guardada exitosamente en la base de datos.", guardada.getId());

        return convertirADTO(guardada);
    }

    public List<ReviewDTO> listaReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO buscarPorId(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Intento fallido de buscar reseña: ID={} no existe", id);
                    return new RecursoNoEncontradoException("Reseña no encontrada con el ID: " + id);
                });
        return convertirADTO(review);
    }

    public List<ReviewDTO> buscarPorIdLibro(Long idLibro) {
        return reviewRepository.findByIdLibro(idLibro).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> buscarPorIdUsuario(Long idUsuario) {
        return reviewRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> buscarPorIdLibroEIdUsuario(Long idLibro, Long idUsuario) {
        return reviewRepository.findByIdLibroAndIdUsuario(idLibro, idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> buscarPorCalificacion(Integer calificacion) {
        return reviewRepository.findByCalificacion(calificacion).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public boolean eliminarReview(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            log.info("Reseña ID={} eliminada del sistema", id);
            return true;
        }
        log.warn("No se pudo eliminar: Reseña ID={} no existe", id);
        return false;
    }

    public ReviewDTO actualizarReview(Long id, ReviewCreateDTO dto) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setComentario(dto.getComentario());
                    review.setCalificacion(dto.getCalificacion());
                    Review actualizada = reviewRepository.save(review);
                    log.info("Reseña ID={} actualizada con éxito", id);
                    return convertirADTO(actualizada);
                })
                .orElseThrow(() -> {
                    log.warn("Intento fallido de actualizar reseña: ID={} no existe", id);
                    return new RecursoNoEncontradoException("No existe la reseña con ID: " + id);
                });
    }

    private ReviewDTO convertirADTO(Review review) {
        return new ReviewDTO(
            review.getId(),
            review.getIdLibro(),
            review.getIdUsuario(),
            review.getComentario(),
            review.getCalificacion()
        );
    }
}
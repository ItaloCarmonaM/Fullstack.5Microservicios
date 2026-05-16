package cl.duoc.review_service.service;

import cl.duoc.review_service.client.CatalogClient;
import cl.duoc.review_service.dto.ReviewCreateDTO;
import cl.duoc.review_service.dto.ReviewDTO;
import cl.duoc.review_service.dto.LibroDTO;
import cl.duoc.review_service.exception.RecursoNoEncontradoException;
import cl.duoc.review_service.exception.ServicioNoDisponibleException;
import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.repository.ReviewRepository;
import feign.FeignException; // Importante para atrapar errores de Feign
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

    public ReviewDTO saveReview(ReviewCreateDTO dto) {
        // Log antes de la llamada externa
        log.info("Solicitando validación de existencia para libro ID={} al servicio Catalog", dto.getIdLibro());

        LibroDTO libro;
        try {

            libro = catalogClient.getLibroById(dto.getIdLibro());
            
            // Log cuando llega la respuesta exitosa
            log.info("Libro recibido correctamente: ID={}, Título='{}'", libro.getId(), libro.getTitulo());

        } catch (FeignException.NotFound e) {
            // Log cuando el recurso no existe en el destino
            log.warn("Servicio Catalog respondió: El libro ID={} no existe", dto.getIdLibro());
            throw new RecursoNoEncontradoException("No se puede guardar la reseña: El libro especificado no existe.");
            
        } catch (FeignException e) {
            // Log cuando falla la red o el servicio destino está caído
            log.error("Error crítico al comunicarse con servicio Catalog: {}", e.getMessage());
            throw new ServicioNoDisponibleException("El servicio de Catálogo no se encuentra disponible temporalmente.");
        }

        Review review = new Review();
        review.setIdLibro(libro.getId()); 
        review.setIdUsuario(dto.getIdUsuario()); 
        review.setComentario(dto.getComentario());
        review.setCalificacion(dto.getCalificacion());

        Review guardada = reviewRepository.save(review);
        
        // Log de evento de negocio exitoso
        log.info("Reseña creada exitosamente: ID={}, para Libro ID={}", guardada.getId(), guardada.getIdLibro());

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
package cl.duoc.review_service.service;

import cl.duoc.review_service.dto.ReviewCreateDTO;
import cl.duoc.review_service.dto.ReviewDTO;
import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ReviewDTO saveReview(ReviewCreateDTO dto) {
        Review review = new Review();
        review.setIdLibro(dto.getIdLibro());
        review.setIdUsuario(dto.getIdUsuario());
        review.setComentario(dto.getComentario());
        review.setCalificacion(dto.getCalificacion());
        
        Review guardada = reviewRepository.save(review);
        return convertirADTO(guardada);
    }

    public List<ReviewDTO> listaReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO buscarPorId(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Reseña no encontrada: " + id));
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
            return true;
        }
        return false;
    }

    public ReviewDTO actualizarReview(Long id, ReviewCreateDTO dto) {
        return reviewRepository.findById(id)
                .map(review -> 
                {
                    review.setComentario(dto.getComentario());
                    review.setCalificacion(dto.getCalificacion());
                    return convertirADTO(reviewRepository.save(review));
                })
                .orElseThrow(() -> new ReviewNotFoundException("No existe la reseña ID: " + id));
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

    public static class ReviewNotFoundException extends RuntimeException {
        public ReviewNotFoundException(String mensaje) {
            super(mensaje);
        }
    }
}
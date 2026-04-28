package cl.duoc.review_service.service;
import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.repository.ReviewRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }
    
    public List<Review> listaReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> buscarPorId(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> buscarPorIdLibro(Long idLibro) {
        return reviewRepository.findByIdLibro(idLibro);
    }

    public List<Review> buscarPorIdUsuario(Long idUsuario) {
        return reviewRepository.findByIdUsuario(idUsuario);
    }

    public List<Review> buscarPorIdLibroEIdUsuario(Long idLibro, Long idUsuario) {
        return reviewRepository.findByIdLibroAndIdUsuario(idLibro, idUsuario);
    }

    public List<Review> buscarPorCalificacion(Integer calificacion) {
        return reviewRepository.findByCalificacion(calificacion);
    }

    // Eliminar review por id
    public boolean eliminarReview(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Actualizar review por id
    public Review actualizarReview(Long id, Review reviewActualizada) {
        Optional<Review> reviewExistente = reviewRepository.findById(id);
        if (reviewExistente.isPresent()) {
            Review review = reviewExistente.get();
            review.setIdLibro(reviewActualizada.getIdLibro());
            review.setIdUsuario(reviewActualizada.getIdUsuario());
            review.setComentario(reviewActualizada.getComentario());
            review.setCalificacion(reviewActualizada.getCalificacion());
            return reviewRepository.save(review);
        }
        return null;
    }      

}

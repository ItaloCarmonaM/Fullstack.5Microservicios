package cl.duoc.review_service.repository;

import cl.duoc.review_service.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByIdLibro(Long idLibro);
    List<Review> findByIdUsuario(Long idUsuario);
    List<Review> findByIdLibroAndIdUsuario(Long idLibro, Long idUsuario);
    List<Review> findByCalificacion(Integer calificacion);
    Review deleteByIdLibroAndIdUsuario(Long idLibro, Long idUsuario);
}

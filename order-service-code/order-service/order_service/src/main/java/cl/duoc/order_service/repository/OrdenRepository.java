package cl.duoc.order_service.repository;

import cl.duoc.order_service.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByIdUsuario(Long idUsuario);
    List<Orden> findByEstadoIgnoreCase(String estado);
}
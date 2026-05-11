package cl.duoc.cart_service.repository;

import cl.duoc.cart_service.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    List<Carrito> findByIdUsuario(Long idUsuario);
    Carrito findByIdUsuarioAndIdLibro(Long idUsuario, Long idLibro);
    void deleteByIdUsuario(Long idUsuario);
}
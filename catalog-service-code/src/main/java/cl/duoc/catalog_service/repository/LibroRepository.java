package cl.duoc.catalog_service.repository;

import cl.duoc.catalog_service.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByAutor(String autor);
    List<Libro> findByTitulo(String titulo);
    List<Libro> findByPrecioLessThan(Double precio);
    List<Libro> findByPrecioGreaterThan(Double precio);
    List<Libro> findByPrecioBetween(Double precioMin, Double precioMax);
}
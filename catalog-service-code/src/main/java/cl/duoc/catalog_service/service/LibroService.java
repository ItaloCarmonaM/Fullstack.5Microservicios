package cl.duoc.catalog_service.service;

import cl.duoc.catalog_service.model.Libro;
import cl.duoc.catalog_service.repository.LibroRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    // Registrar un nuevo libro (Las validaciones se realizan en el controlador con
    // @Valid y en el modelo con anotaciones de validación)
    public Libro registrarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    // Listar todos los libros
    public List<Libro> listaLibros() {
        return libroRepository.findAll();
    }

    // Buscar libro por id
    public Optional<Libro> buscarPorId(Long id) {
        return libroRepository.findById(id);
    }

    // Buscar libro por autor
    public List<Libro> buscarPorAutor(String autor) {
        return libroRepository.findByAutor(autor);
    }

    // Buscar libro por título
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTitulo(titulo);
    }

    // Buscar libro por precio menor a un valor
    public List<Libro> buscarPorPrecioMenor(Double precio) {
        return libroRepository.findByPrecioLessThan(precio);
    }

    // Buscar libro por precio mayor a un valor
    public List<Libro> buscarPorPrecioMayor(Double precio) {
        return libroRepository.findByPrecioGreaterThan(precio);
    }

    // Buscar libro por precio entre dos valores
    public List<Libro> buscarPorPrecioEntre(Double precioMin, Double precioMax) {
        return libroRepository.findByPrecioBetween(precioMin, precioMax);
    }

    // Eliminar libro por id
    public boolean eliminarLibro(Long id) {
        if (libroRepository.existsById(id)) {
            libroRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Libro actualizarLibro(Long id, Libro libroActualizado) {
        Optional<Libro> libroExistenteOpt = libroRepository.findById(id);
        if (libroExistenteOpt.isPresent()) {
            Libro libroExistente = libroExistenteOpt.get();
            libroExistente.setTitulo(libroActualizado.getTitulo());
            libroExistente.setAutor(libroActualizado.getAutor());
            libroExistente.setPrecio(libroActualizado.getPrecio());
            return libroRepository.save(libroExistente);
        }
        return null;
    }

}

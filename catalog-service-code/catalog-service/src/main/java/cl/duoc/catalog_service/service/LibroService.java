package cl.duoc.catalog_service.service;

import cl.duoc.catalog_service.dto.LibroCreateDTO;
import cl.duoc.catalog_service.dto.LibroDTO;
import cl.duoc.catalog_service.model.Libro;
import cl.duoc.catalog_service.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    // Crear: Recibe CreateDTO -> Guarda Entidad -> Retorna DTO salida
    public LibroDTO registrarLibro(LibroCreateDTO dto) {
        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setCategoria(dto.getCategoria());
        libro.setEditorial(dto.getEditorial());
        libro.setTapa(dto.getTapa());
        libro.setPrecio(dto.getPrecio());

        Libro guardado = libroRepository.save(libro);
        return convertirADTO(guardado);
    }

    // Listar todos: Retorna lista de DTOs
    public List<LibroDTO> listaLibros() {
        return libroRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar por ID: Retorna DTO o lanza excepción
    public LibroDTO buscarPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new LibroNotFoundException(id));
        return convertirADTO(libro);
    }

    // Buscar por autor
    public List<LibroDTO> buscarPorAutor(String autor) {
        return libroRepository.findByAutorIgnoreCase(autor).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar por título
    public List<LibroDTO> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloIgnoreCase(titulo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar por precio menor
    public List<LibroDTO> buscarPorPrecioMenor(Double precio) {
        return libroRepository.findByPrecioLessThan(precio).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar por precio mayor
    public List<LibroDTO> buscarPorPrecioMayor(Double precio) {
        return libroRepository.findByPrecioGreaterThan(precio).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar por precio entre
    public List<LibroDTO> buscarPorPrecioEntre(Double precioMin, Double precioMax) {
        return libroRepository.findByPrecioBetween(precioMin, precioMax).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar por categoría
    public List<LibroDTO> buscarPorCategoria(String categoria) {
        return libroRepository.findByCategoriaIgnoreCase(categoria).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar por editorial
    public List<LibroDTO> buscarPorEditorial(String editorial) {
        return libroRepository.findByEditorialIgnoreCase(editorial).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Eliminar: Mantiene lógica de boolean
    public boolean eliminarLibro(Long id) {
        if (libroRepository.existsById(id)) {
            libroRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Actualizar: Recibe CreateDTO (porque trae los mismos campos) y retorna DTO salida
    public LibroDTO actualizarLibro(Long id, LibroCreateDTO dto) {
        return libroRepository.findById(id)
                .map(libroExistente -> {
                    libroExistente.setTitulo(dto.getTitulo());
                    libroExistente.setAutor(dto.getAutor());
                    libroExistente.setCategoria(dto.getCategoria());
                    libroExistente.setEditorial(dto.getEditorial());
                    libroExistente.setTapa(dto.getTapa());
                    libroExistente.setPrecio(dto.getPrecio());
                    Libro actualizado = libroRepository.save(libroExistente);
                    return convertirADTO(actualizado);
                })
                .orElseThrow(() -> new LibroNotFoundException(id));
    }

    // --- MÉTODOS DE APOYO (MAPPERS) ---

    private LibroDTO convertirADTO(Libro libro) {
        return new LibroDTO(
            libro.getId(),
            libro.getTitulo(),
            libro.getAutor(),
            libro.getCategoria(),
            libro.getEditorial(),
            libro.getTapa(),
            libro.getPrecio()
        );
    }

    // Excepción personalizada interna
    public static class LibroNotFoundException extends RuntimeException {
        public LibroNotFoundException(Long id) {
            super("No se encontró el libro con id: " + id);
        }
    }
}
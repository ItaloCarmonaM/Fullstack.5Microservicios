package cl.duoc.catalog_service.service;

import cl.duoc.catalog_service.dto.LibroCreateDTO;
import cl.duoc.catalog_service.dto.LibroDTO;
import cl.duoc.catalog_service.exceptions.LibroNotFoundException;
import cl.duoc.catalog_service.model.Libro;
import cl.duoc.catalog_service.repository.LibroRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private static final Logger log = LoggerFactory.getLogger(LibroService.class);

    @Autowired
    private LibroRepository libroRepository;

    public LibroDTO registrarLibro(LibroCreateDTO dto) {
        log.info("Solicitando registro de nuevo libro titulado: '{}'", dto.getTitulo());
        
        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setCategoria(dto.getCategoria());
        libro.setEditorial(dto.getEditorial());
        libro.setTapa(dto.getTapa());
        libro.setPrecio(dto.getPrecio());

        Libro guardado = libroRepository.save(libro);
        
        log.info("Libro creado exitosamente: ID={}, Título='{}'", guardado.getId(), guardado.getTitulo());
        return convertirADTO(guardado);
    }

    public List<LibroDTO> listaLibros() {
        log.info("Recuperando el catálogo completo de libros de la base de datos");
        List<Libro> libros = libroRepository.findAll();
        log.info("Catálogo recuperado. Total de libros encontrados: {}", libros.size());
        return libros.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public LibroDTO buscarPorId(Long id) {
        log.info("Buscando detalles del libro con ID: {}", id);
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Error en consulta: El libro con ID={} no existe en el catálogo", id);
                    return new LibroNotFoundException(id);
                });
        log.info("Libro localizado con éxito: '{}'", libro.getTitulo());
        return convertirADTO(libro);
    }

    public List<LibroDTO> buscarPorAutor(String autor) {
        log.info("Filtrando catálogo por el autor: '{}'", autor);
        return libroRepository.findByAutorIgnoreCase(autor).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorTitulo(String titulo) {
        log.info("Buscando coincidencias exactas para el título: '{}'", titulo);
        return libroRepository.findByTituloIgnoreCase(titulo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorPrecioMenor(Double precio) {
        log.info("Filtrando libros con precio menor a: ${}", precio);
        return libroRepository.findByPrecioLessThan(precio).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorPrecioMayor(Double precio) {
        log.info("Filtrando libros con precio mayor a: ${}", precio);
        return libroRepository.findByPrecioGreaterThan(precio).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorPrecioEntre(Double precioMin, Double precioMax) {
        log.info("Filtrando libros dentro del rango de precios: ${} y ${}", precioMin, precioMax);
        return libroRepository.findByPrecioBetween(precioMin, precioMax).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorCategoria(String categoria) {
        log.info("Buscando libros pertenecientes a la categoría: '{}'", categoria);
        return libroRepository.findByCategoriaIgnoreCase(categoria).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorEditorial(String editorial) {
        log.info("Buscando publicaciones asociadas a la editorial: '{}'", editorial);
        return libroRepository.findByEditorialIgnoreCase(editorial).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public boolean eliminarLibro(Long id) {
        log.info("Solicitud para eliminar del catálogo el libro con ID: {}", id);
        if (libroRepository.existsById(id)) {
            libroRepository.deleteById(id);
            log.info("Evento de negocio: Libro con ID={} eliminado correctamente", id);
            return true;
        }
        log.warn("No se pudo eliminar: El libro con ID={} no se encuentra registrado", id);
        return false;
    }

    public LibroDTO actualizarLibro(Long id, LibroCreateDTO dto) {
        log.info("Iniciando actualización completa para el libro con ID: {}", id);
        return libroRepository.findById(id)
                .map(libroExistente -> {
                    libroExistente.setTitulo(dto.getTitulo());
                    libroExistente.setAutor(dto.getAutor());
                    libroExistente.setCategoria(dto.getCategoria());
                    libroExistente.setEditorial(dto.getEditorial());
                    libroExistente.setTapa(dto.getTapa());
                    libroExistente.setPrecio(dto.getPrecio());
                    
                    Libro actualizado = libroRepository.save(libroExistente);
                    log.info("Evento de negocio: Libro con ID={} modificado exitosamente", id);
                    return convertirADTO(actualizado);
                })
                .orElseThrow(() -> {
                    log.warn("Fallo al actualizar: No se encontró registro para el ID={}", id);
                    return new LibroNotFoundException(id);
                });
    }

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
}
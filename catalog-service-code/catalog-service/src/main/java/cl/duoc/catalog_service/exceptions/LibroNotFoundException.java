package cl.duoc.catalog_service.exceptions;

public class LibroNotFoundException extends RecursoNoEncontradoException {
    public LibroNotFoundException(Long id) {
        super("No se encontró el libro con id: " + id); 
    }
}
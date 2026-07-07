package cl.duoc.catalog_service.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class LibroNotFoundException extends RecursoNoEncontradoException {
    public LibroNotFoundException(Long id) {
        super("No se encontró el libro con id: " + id); 
    }
}
package cl.duoc.order_service.service;

import cl.duoc.order_service.dto.OrdenCreateDTO;
import cl.duoc.order_service.dto.OrdenDTO;
import cl.duoc.order_service.model.Orden;
import cl.duoc.order_service.repository.OrdenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;

    public OrdenService(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    public OrdenDTO crearOrden(OrdenCreateDTO dto) {
        Orden orden = new Orden();
        orden.setIdUsuario(dto.getIdUsuario());
        orden.setTotal(dto.getTotal());
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setEstado("PENDIENTE");
        
        return convertirADTO(ordenRepository.save(orden));
    }

    public List<OrdenDTO> listarTodas() {
        return ordenRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public OrdenDTO buscarPorId(Long id) {
        return ordenRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new OrdenNotFoundException(id));
    }

    public List<OrdenDTO> buscarPorUsuario(Long idUsuario) {
        return ordenRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Actualizar Estado usando lógica de PUT
    public OrdenDTO actualizarEstado(Long id, String nuevoEstado) {
        return ordenRepository.findById(id).map(o -> {
            o.setEstado(nuevoEstado.toUpperCase());
            return convertirADTO(ordenRepository.save(o));
        }).orElseThrow(() -> new OrdenNotFoundException(id));
    }

    // Método Delete
    public boolean eliminarOrden(Long id) {
        if (ordenRepository.existsById(id)) {
            ordenRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private OrdenDTO convertirADTO(Orden o) {
        return new OrdenDTO(o.getId(), o.getIdUsuario(), o.getFechaCreacion(), o.getTotal(), o.getEstado());
    }

    public static class OrdenNotFoundException extends RuntimeException {
        public OrdenNotFoundException(Long id) { super("Orden no encontrada: " + id); }
    }
}
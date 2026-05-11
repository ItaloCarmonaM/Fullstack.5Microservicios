package cl.duoc.cart_service.service;

import cl.duoc.cart_service.dto.CarritoCreateDTO;
import cl.duoc.cart_service.dto.CarritoDTO;
import cl.duoc.cart_service.model.Carrito;
import cl.duoc.cart_service.repository.CarritoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;

    public CarritoService(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public CarritoDTO agregarItem(CarritoCreateDTO dto) {
        // Regla: Si el usuario ya tiene este libro, sumamos la cantidad
        Carrito existente = carritoRepository.findByIdUsuarioAndIdLibro(dto.getIdUsuario(), dto.getIdLibro());
        
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + dto.getCantidad());
            return convertirADTO(carritoRepository.save(existente));
        }

        Carrito nuevo = new Carrito();
        nuevo.setIdUsuario(dto.getIdUsuario());
        nuevo.setIdLibro(dto.getIdLibro());
        nuevo.setCantidad(dto.getCantidad());
        return convertirADTO(carritoRepository.save(nuevo));
    }

    public List<CarritoDTO> obtenerPorUsuario(Long idUsuario) {
        return carritoRepository.findByIdUsuario(idUsuario).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void vaciarCarrito(Long idUsuario) {
        carritoRepository.deleteByIdUsuario(idUsuario);
    }

    public boolean eliminarItem(Long id) {
        if (carritoRepository.existsById(id)) {
            carritoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public CarritoDTO actualizarCantidad(Long id, Integer nuevaCantidad) {
        return carritoRepository.findById(id).map(c -> {
            c.setCantidad(nuevaCantidad);
            return convertirADTO(carritoRepository.save(c));
        }).orElseThrow(() -> new RuntimeException("Item de carrito no encontrado"));
    }

    private CarritoDTO convertirADTO(Carrito c) {
        return new CarritoDTO(c.getId(), c.getIdUsuario(), c.getIdLibro(), c.getCantidad());
    }
}
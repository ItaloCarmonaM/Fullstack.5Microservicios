package cl.duoc.order_service.client;

import cl.duoc.order_service.dto.CarritoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(
    name = "cart-service-container",
    url = "${cart.service.url}"
)
public interface CarritoClient {

    @GetMapping("/api/v1/carritos/usuario/{idUsuario}")
    List<CarritoDTO> obtenerCarrito(@PathVariable("idUsuario") Long idUsuario);

    @DeleteMapping("/api/v1/carritos/usuario/{idUsuario}")
    void vaciar(@PathVariable("idUsuario") Long idUsuario);
}
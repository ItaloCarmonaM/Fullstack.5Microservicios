package cl.duoc.order_service.client;

import cl.duoc.order_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "user-service-externo-orders",
    url = "${user.service.url:http://localhost:8080}"
)
public interface UserClient {

    @GetMapping("/api/v1/usuarios/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
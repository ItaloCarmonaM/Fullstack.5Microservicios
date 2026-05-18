package cl.duoc.cart_service.client;

import cl.duoc.cart_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "user-service-externo-cart",
    url = "${user.service.url:http://localhost:8080}"
)
public interface UserClient {

    @GetMapping("/api/v1/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
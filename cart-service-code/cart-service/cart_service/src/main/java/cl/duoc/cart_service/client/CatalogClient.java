package cl.duoc.cart_service.client;

import cl.duoc.cart_service.dto.LibroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog-service-container", url = "${catalog.service.url}")
public interface CatalogClient {

    @GetMapping("/api/v2/libros/{id}")
    LibroDTO buscarPorId(@PathVariable("id") Long id);
}
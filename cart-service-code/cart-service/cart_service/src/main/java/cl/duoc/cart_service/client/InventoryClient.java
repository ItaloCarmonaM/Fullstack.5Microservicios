package cl.duoc.cart_service.client;

import cl.duoc.cart_service.dto.InventoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service-container", url = "${inventory.service.url}")
public interface InventoryClient {

    @GetMapping("/api/v2/inventories/idLibro/{idLibro}")
    InventoryDTO buscarPorIdLibro(@PathVariable("idLibro") Long idLibro);
}
package cl.duoc.inventory_service.repository;
import cl.duoc.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByIdLibro(Long idLibro);
    
}

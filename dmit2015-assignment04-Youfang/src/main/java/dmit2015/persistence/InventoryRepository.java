package dmit2015.persistence;

import dmit2015.entity.Inventory;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository(dataStore = "oracle-jpa-co-pu")
public interface InventoryRepository {
    @Query("SELECT i FROM Inventory i JOIN FETCH i.store WHERE i.product.id = :productId")
    List<Inventory> findInventoryByProductId(Long productId);
}

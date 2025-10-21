package dmit2015.persistence;

import dmit2015.entity.Product;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository(dataStore = "oracle-jpa-co-pu")
public interface ProductRepository {
    @Query("SELECT p FROM Product p WHERE p.productName LIKE :namePattern")
    List<Product> findProductsByName(String namePattern);
}

package dmit2015.persistence;

import dmit2015.entity.Product;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository(dataStore = "oracle-jpa-co-pu")
public interface ICustomerOrdersRepository {
    @Query("TODO: Write a JDQL query to return a list of Product matching the namePattern")
    List<Product> findProductsByName(String namePattern);
}

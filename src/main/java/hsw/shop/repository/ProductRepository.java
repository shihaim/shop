package hsw.shop.repository;

import hsw.shop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p.name from Product p where p.id = :id")
    String findNameById(@Param("id") Long id);
}

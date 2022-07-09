package hsw.shop.repository;

import hsw.shop.domain.Product;
import hsw.shop.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("select p.name from Product p where p.id = :id")
    String findNameById(@Param("id") Long id);

    @Query("select p from Product p" +
            " join fetch p.productImage pi" +
            " where p.id = :productId")
    Optional<Product> findProductById(@Param("productId") Long productId);

    @Query("select p from Product p" +
            " join fetch p.productImage pi")
    List<Product> findByProductList();

    @Query("select p from Product p" +
            " join fetch p.productImage pi" +
            " where p.id in :productIds")
    List<Product> findImageByProductIds(@Param("productIds") List<Long> productIds);
}

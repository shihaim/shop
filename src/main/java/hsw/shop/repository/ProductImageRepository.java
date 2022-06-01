package hsw.shop.repository;

import hsw.shop.domain.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ProductImageRepository {

    private final EntityManager em;

    public void save(ProductImage productImage) {
        em.persist(productImage);
    }
}

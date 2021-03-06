package hsw.shop.repository;

import hsw.shop.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductJpaRepository {

    private final EntityManager em;

    public void save(Product product) {
        em.persist(product);
    }

    public Product findOne(Long id) {
        return em.find(Product.class, id);
    }

    public String findByName(Long id) {
        return em.createQuery("select p.name from Product p where p.id = :id", String.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }
}

package hsw.shop.repository;

import hsw.shop.domain.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartJpaRepository {

    private final EntityManager em;

    public void save(Cart cart) {
        em.persist(cart);
    }

    public Cart findOne(Long id) {
        return em.find(Cart.class, id);
    }

    public List<Cart> findAll() {
        return em.createQuery("select c from Cart c", Cart.class)
                .getResultList();
    }

    public List<Cart> findAllByMemberId(Long memberId) {
        return em.createQuery(
                "select c from Cart c" +
                        " join c.member m" +
                        " on m.memberId = :memberId")
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public void remove(Cart cart) {
        em.remove(cart);
    }
}

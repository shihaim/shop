package hsw.shop.repository;

import hsw.shop.domain.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class DeliveryRepository {

    private final EntityManager em;

    public void save(Delivery delivery) {
        em.persist(delivery);
    }
}

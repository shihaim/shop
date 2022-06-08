package hsw.shop.repository;

import hsw.shop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByMemberId(Long memberId) {
        return em.createQuery(
                        "select o from Order o" +
                                " join o.member m" +
                                " on o.member.memberId = m.memberId", Order.class)
                .getResultList();
    }

    //주문 조회
//    public List<Order> findAll(OrderSearch orderSearch) {}
}

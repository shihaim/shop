package hsw.shop.repository;

import hsw.shop.domain.Order;
import hsw.shop.domain.OrderDetail;
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
                                " on o.member.memberId = :memberId", Order.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public List<OrderDetail> findOrderDetailsByOrderId(Long orderId) {
        return em.createQuery(
                        "select od from OrderDetail od" +
                                " join od.order o" +
                                " join od.product p" +
                                " on od.order.id = :orderId", OrderDetail.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    //주문 조회
//    public List<Order> findAll(OrderSearch orderSearch) {}
}

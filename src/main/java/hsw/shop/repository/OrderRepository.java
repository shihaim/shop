package hsw.shop.repository;

import hsw.shop.domain.Order;
import hsw.shop.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d" +
            " where m.memberId = :memberId")
    List<Order> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select od from OrderDetail od" +
            " join fetch od.order o" +
            " join fetch od.product p" +
            " join fetch p.productImage pi" +
            " where o.id = :orderId")
    Optional<List<OrderDetail>> findOrderDetailsByOrderId(@Param("orderId") Long orderId);
}

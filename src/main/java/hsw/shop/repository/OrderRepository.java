package hsw.shop.repository;

import hsw.shop.domain.Order;
import hsw.shop.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o" +
            " join o.member m" +
            " on m.id = :memberId")
    List<Order> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("select od from OrderDetail od" +
            " join od.order o" +
            " join od.product p" +
            " on o.id = :orderId")
    List<OrderDetail> findOrderDetailsByOrderId(@Param("orderId") Long orderId);
}

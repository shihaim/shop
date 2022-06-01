package hsw.shop.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    protected Order() {
    }

    @Builder
    public Order(Member member, List<OrderDetail> orderDetails, LocalDateTime orderDate, OrderStatus status, Delivery delivery) {
        this.member = member;
        this.orderDetails = orderDetails;
        this.orderDate = orderDate;
        this.status = status;
        this.delivery = delivery;
    }

    //생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderDetail... orderDetails) {
        return Order.builder()
                    .member(member)
                    .orderDetails(Arrays.stream(orderDetails).collect(Collectors.toList()))
                    .orderDate(LocalDateTime.now())
                    .status(OrderStatus.ORDER)
                    .delivery(delivery)
                    .build();
    }

    //주문 취소
    public void cancel() {

        if (delivery.getDeliveryStatus() == DeliveryStatus.COMPLETE) {
            throw new IllegalStateException("이미 배송된 상품입니다.");
        }

        this.status = OrderStatus.CANCEL;

        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.cancel();
        }
    }
}

package hsw.shop.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 필드 초기화를 했는데도 불구하고 빌더 패턴을 사용하면
     * 리스트 초기화가 안되서 NullPointerException 발생
     * 빌더에서 초기화를 한번 더 진행함.
     */
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

    /**
     * 원래 코드
     * this.orderDetails.add(orderDetail);
     * orderDetail.builder().order(this);
     *
     * 그런데 orderDetail의 order가 자꾸 null 값을 저장하는 바람에 골치가 아팠는데,
     * 연관관계 메서드 만든 후 해보니 잘 들어감.
     * 빌더 패턴에 대한 이해가 부족
     */
    public void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
        orderDetail.addOrder(this);
    }

    //생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderDetail orderDetail) {

        Order order = Order.builder()
                .member(member)
                .orderDetails(new ArrayList<>())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .delivery(delivery)
                .build();

        order.addOrderDetail(orderDetail);

        return order;
    }

    //List를 통해서 받는 방법말고는 생각이 안나서 가변인자로 받으려니 해결이 힘들어서 오버로딩 이용
    public static Order createOrder(Member member, Delivery delivery, List<OrderDetail> orderDetails) {

        Order order = Order.builder()
                .member(member)
                .orderDetails(new ArrayList<>())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .delivery(delivery)
                .build();

        for (OrderDetail orderDetail : orderDetails) {
            order.addOrderDetail(orderDetail);
        }

        return order;
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

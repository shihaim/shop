package hsw.shop.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class OrderDetail {

    @Id
    @GeneratedValue
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int price; //주문 금액
    private int count; //주문 수량

    protected OrderDetail() {
    }

    @Builder
    public OrderDetail(Order order, Product product, int price, int count) {
        this.order = order;
        this.product = product;
        this.price = price;
        this.count = count;
    }

    //생성 메서드
    public static OrderDetail createOrderDetail(Product product, int orderPrice, int count) {
        OrderDetail orderDetail = OrderDetail.builder()
                .product(product)
                .price(orderPrice)
                .count(count)
                .build();

        product.removeStock(count);
        return orderDetail;
    }

    //상세 주문 취소
    public void cancel() {
        getProduct().addStock(count);
    }
}

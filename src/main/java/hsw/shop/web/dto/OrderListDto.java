package hsw.shop.web.dto;

import hsw.shop.domain.DeliveryStatus;
import hsw.shop.domain.Order;
import hsw.shop.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderListDto {

    private Long id;
    private OrderStatus status;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime orderDate;

    public OrderListDto(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.deliveryStatus = order.getDelivery().getDeliveryStatus();
        this.orderDate = order.getOrderDate();
    }
}

package hsw.shop.web.dto;

import hsw.shop.domain.OrderDetail;
import lombok.Getter;

@Getter
public class OrderDetailDto {

    private String filename;
    private String productName;
    private int price;
    private int count;

    public OrderDetailDto(OrderDetail orderDetail) {
        this.filename = orderDetail.getProduct().getProductImage().getStoreImageName();
        this.productName = orderDetail.getProduct().getName();
        this.price = orderDetail.getPrice();
        this.count = orderDetail.getCount();
    }
}

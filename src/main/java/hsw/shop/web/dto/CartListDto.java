package hsw.shop.web.dto;

import hsw.shop.domain.Cart;
import lombok.Getter;

@Getter
public class CartListDto {
    private Long id;
    private String storeImageName;
    private String productName;
    private int count;

    public CartListDto(Cart cart) {
        this.id = cart.getId();
        this.storeImageName = cart.getProduct().getProductImage().getStoreImageName();
        this.productName = cart.getProduct().getName();
        this.count = cart.getCount();
    }
}

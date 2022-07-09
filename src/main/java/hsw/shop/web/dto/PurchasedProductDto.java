package hsw.shop.web.dto;

import hsw.shop.domain.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchasedProductDto {

    private Long id;
    private String storeImageName;

    public PurchasedProductDto(Product product) {
        this.id = product.getId();
        this.storeImageName = product.getProductImage().getStoreImageName();
    }
}

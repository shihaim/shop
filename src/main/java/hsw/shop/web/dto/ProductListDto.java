package hsw.shop.web.dto;

import hsw.shop.domain.Product;
import lombok.Getter;

@Getter
public class ProductListDto {

    private Long id;
    private String storeImageName;
    private String name;
    private int price;

    public ProductListDto() {
    }

    public ProductListDto(Product product) {
        this.id = product.getId();
        this.storeImageName = product.getProductImage().getStoreImageName();
        this.name = product.getName();
        this.price = product.getPrice();
    }
}

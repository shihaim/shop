package hsw.shop.web.dto;

import hsw.shop.domain.Product;
import lombok.Getter;

@Getter
public class ProductDetailDto {

    private Long id;
    private String storeImageName;
    private String name;
    private int price;
    private String description;

    protected ProductDetailDto() {
    }

    public ProductDetailDto(Product product) {
        this.id = product.getId();
        this.storeImageName = product.getProductImage().getStoreImageName();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }
}

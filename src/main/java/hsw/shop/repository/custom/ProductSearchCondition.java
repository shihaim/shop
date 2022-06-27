package hsw.shop.repository.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCondition {
    private String productName;

    public ProductSearchCondition() {
    }

    public ProductSearchCondition(String productName) {
        this.productName = productName;
    }
}

package hsw.shop.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductIdsDto {

    private List<Long> productIds;

    public ProductIdsDto() {
    }

    public ProductIdsDto(List<Long> productIds) {
        this.productIds = productIds;
    }
}

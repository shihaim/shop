package hsw.shop.web.dto;

import hsw.shop.domain.ProductImage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductImageCreateDto {

    private String originalImageName;

    private String storeImageName;

    @Builder
    public ProductImageCreateDto(String originalImageName, String storeImageName) {
        this.originalImageName = originalImageName;
        this.storeImageName = storeImageName;
    }

    public ProductImage toEntity() {
        return ProductImage.builder()
                .originalImageName(originalImageName)
                .storeImageName(storeImageName)
                .build();
    }
}

package hsw.shop.web.dto;

import hsw.shop.domain.ProductImage;
import lombok.Builder;
import lombok.Getter;

/**
 * TODO
 * 상품 수정 검증(수정시 이미지 변경은 일단 보류)
 * 1. null 값 검증
 */
@Getter
public class ProductUpdateDto {

    private ProductImage productImage;

    private String name;

    private int price;

    private String description;

    @Builder
    public ProductUpdateDto(ProductImage productImage, String name, int price, String description) {
        this.productImage = productImage;
        this.name = name;
        this.price = price;
        this.description = description;
    }
}

package hsw.shop.web.dto;

import hsw.shop.domain.Product;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

/**
 * TODO
 * 상품 생성 검증
 * 1. null값 검증
 */
@Getter
public class ProductCreateDto {

    private MultipartFile productImage;

    private String name;

    private int price;

    private int stockQuantity;

    private String description;

    @Builder
    public ProductCreateDto(MultipartFile productImage, String name, int price, int stockQuantity, String description) {
        this.productImage = productImage;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .description(description)
                .build();
    }
}

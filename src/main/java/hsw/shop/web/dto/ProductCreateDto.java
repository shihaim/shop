package hsw.shop.web.dto;

import hsw.shop.domain.Product;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
public class ProductCreateDto {

    @NotNull
    private MultipartFile productImage;

    @NotNull
    private String name;

    @NotNull
    private int price;

    @NotNull
    private int stockQuantity;

    @NotNull
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

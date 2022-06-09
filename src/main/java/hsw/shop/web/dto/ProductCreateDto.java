package hsw.shop.web.dto;

import hsw.shop.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ProductCreateDto {

    @NotEmpty(message = "상품 이름을 입력해주세요.")
    private String name;

    @Range(min = 1000, max = 1000000)
    private int price;

    @Range(min = 1, max = 9999)
    private int stockQuantity;

    @NotEmpty(message = "상품 설명을 입력해주세요.")
    private String description;

    //NotEmpty 어노테이션이 안먹힘. 글로벌 오류로 처리해야 할 듯.
    private MultipartFile productImage;

    @NotEmpty
    public ProductCreateDto() {
    }

    @Builder
    public ProductCreateDto(String name, int price, int stockQuantity, String description, MultipartFile productImage) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.productImage = productImage;
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

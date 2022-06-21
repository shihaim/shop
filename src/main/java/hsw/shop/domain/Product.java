package hsw.shop.domain;

import hsw.shop.exception.NotEnoughStockException;
import hsw.shop.web.dto.ProductUpdateDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Product extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "product_image_id")
    private ProductImage productImage;

    private String name;

    private int price;

    private int stockQuantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    protected Product() {
    }

    @Builder
    public Product(ProductImage productImage, String name, int price, int stockQuantity, String description) {
        this.productImage = productImage;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    public static Product createProduct(ProductImage productImage, Product product) {
        return Product.builder()
                .productImage(productImage)
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .build();
    }

    //재고 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    //재고 감소
    public void removeStock(int quantity) {
        int restStockQuantity = this.getStockQuantity() - quantity;
        if (restStockQuantity < 0) {
            throw new NotEnoughStockException("현재 재고 수보다 주문량이 많습니다!");
        }

        this.stockQuantity = restStockQuantity;
    }

    //상품 수정(재고 수정은 따로 만드는 게 좋을 듯?..)
    public void update(ProductUpdateDto productUpdateDto) {
        this.productImage = productUpdateDto.getProductImage();
        this.name = productUpdateDto.getName();
        this.price = productUpdateDto.getPrice();
        this.description = productUpdateDto.getDescription();
    }
}

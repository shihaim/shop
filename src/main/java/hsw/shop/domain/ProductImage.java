package hsw.shop.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class ProductImage {

    @Id
    @GeneratedValue
    @Column(name = "product_image_id")
    private Long id;

    private String originalImageName;

    private String storeImageName;

    protected ProductImage() {
    }

    @Builder
    public ProductImage(String originalImageName, String storeImageName) {
        this.originalImageName = originalImageName;
        this.storeImageName = storeImageName;
    }

    public static ProductImage createProductImage(String originalImageName, String storeImageName) {
        return ProductImage.builder()
                .originalImageName(originalImageName)
                .storeImageName(storeImageName)
                .build();
    }
}

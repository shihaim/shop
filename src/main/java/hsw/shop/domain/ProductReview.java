package hsw.shop.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ProductReview {

    @Id
    @GeneratedValue
    @Column(name = "product_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(columnDefinition = "TEXT")
    private String productReview;

    @Builder
    public ProductReview(Member member, Product product, String productReview) {
        this.member = member;
        this.product = product;
        this.productReview = productReview;
    }


    public static ProductReview createProductReview(Member member, Product product, String productReview) {
        return ProductReview.builder()
                .member(member)
                .product(product)
                .productReview(productReview)
                .build();
    }
}

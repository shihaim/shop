package hsw.shop.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class Cart extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int count;

    protected Cart() {
    }

    @Builder
    public Cart(Member member, Product product, int count) {
        this.member = member;
        this.product = product;
        this.count = count;
    }

    //생성 메서드
    public static Cart createCart(Member member, Product product, int count) {
        return Cart.builder()
                    .member(member)
                    .product(product)
                    .count(count)
                    .build();
    }
}

package hsw.shop.service;

import hsw.shop.domain.Cart;
import hsw.shop.domain.Member;
import hsw.shop.domain.Product;
import hsw.shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    //상품 담기
    public Long put(Long memberId, Long productId, int count) {

        //엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다. memberId = " + memberId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다. productId = " + productId));

        //장바구니 상품 생성
        Cart cart = Cart.createCart(member, product, count);

        cartRepository.save(cart);

        return cart.getId();
    }

    //담기 취소
    public void cancel(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다. cartId = " + cartId));

        cartRepository.delete(cart);
    }
}

package hsw.shop.service;

import hsw.shop.domain.Member;
import hsw.shop.domain.Product;
import hsw.shop.domain.ProductReview;
import hsw.shop.repository.MemberRepository;
import hsw.shop.repository.ProductRepository;
import hsw.shop.repository.ProductReviewRepository;
import hsw.shop.web.dto.ReviewSaveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductReviewService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;

    public Long reviewSave(ReviewSaveDto reviewSaveDto) {
        Member member = memberRepository.findById(reviewSaveDto.getMemberId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다. memberId = " + reviewSaveDto.getMemberId()));
        Product product = productRepository.findById(reviewSaveDto.getProductId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다. productId = " + reviewSaveDto.getProductId()));

        ProductReview productReview = ProductReview.createProductReview(member, product, reviewSaveDto.getProductReview());
        productReviewRepository.save(productReview);

        return productReview.getId();
    }
}

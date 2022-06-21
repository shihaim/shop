package hsw.shop.service;

import hsw.shop.domain.Product;
import hsw.shop.domain.ProductImage;
import hsw.shop.repository.ProductImageRepository;
import hsw.shop.repository.ProductRepository;
import hsw.shop.web.dto.ProductCreateDto;
import hsw.shop.web.dto.ProductUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ImageStore imageStore;

    //상품 저장
    @Transactional
    public Long saveProduct(ProductCreateDto productCreateDto) throws IOException {

        /**
         * CascadeType.PERSIST를 설정하지 않고 하려면 직접 트랜잭션해야함.
         * 영속성 전이 사용시 주의 사항
         *  - 부모 객체를 기준으로 자식 객체(연관 관계 매핑이 되어 있는 객체)에도 연관 관계 매핑이 되어 있을 경우
         *    사용하면 안된다.
         */
        //상품 이미지 생성
        ProductImage productImage = imageStore.storeFile(productCreateDto.getProductImage());
        productImageRepository.save(productImage);

        //상품 생성
        Product product = Product.createProduct(productImage, productCreateDto.toEntity());
        productRepository.save(product);

        return product.getId();
    }

    //상품 수정
    @Transactional
    public void updateProduct(Long productId, ProductUpdateDto productUpdateDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다. productId = " + productId));

        product.update(productUpdateDto);
    }

    //상품 조회
    public Product findProduct(Long productId) {
        return productRepository.findProductById(productId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다. productId = " + productId));
    }
}

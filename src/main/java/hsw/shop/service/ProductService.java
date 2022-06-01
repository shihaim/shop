package hsw.shop.service;

import hsw.shop.domain.Product;
import hsw.shop.domain.ProductImage;
import hsw.shop.repository.ProductImageRepository;
import hsw.shop.repository.ProductRepository;
import hsw.shop.web.dto.ProductCreateDto;
import hsw.shop.web.dto.ProductUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ImageStore imageStore;

    //상품 저장
    public Long saveProduct(ProductCreateDto productCreateDto) throws IOException {

        //상품 이미지 생성
        ProductImage productImage = imageStore.storeFile(productCreateDto.getProductImage());
        productImageRepository.save(productImage);

        //상품 생성
        Product product = Product.createProduct(productImage, productCreateDto.toEntity());
        productRepository.save(product);

        return product.getId();
    }

    //상품 수정
    public void updateProduct(Long productId, ProductUpdateDto productUpdateDto) {
        Product product = productRepository.findOne(productId);

        product.update(productUpdateDto);
    }

    //상품 조회
    @Transactional(readOnly = true)
    public Product findProduct(Long productId) {
        return productRepository.findOne(productId);
    }

    //상품 전체 조회
    @Transactional(readOnly = true)
    public List<Product> findProducts() {
        return productRepository.findAll();
    }
}

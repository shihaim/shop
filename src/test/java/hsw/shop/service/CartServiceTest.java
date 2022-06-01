package hsw.shop.service;

import hsw.shop.domain.Cart;
import hsw.shop.domain.Member;
import hsw.shop.domain.Product;
import hsw.shop.domain.ProductImage;
import hsw.shop.repository.CartRepository;
import hsw.shop.repository.MemberRepository;
import hsw.shop.repository.ProductRepository;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.ProductCreateDto;
import hsw.shop.web.dto.ProductImageCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartRepository cartRepository;

    @Test
    public void 장바구니_상품담기() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream("C:/Temp/hswshop/product_image/b6fbfd7d-db83-4c74-8661-175f2f03ce9b.png"));
        Product product = createProduct(productImage, "JPA", 15000, 10, "김영한님의 JPA 저서, JPA를 배우는 모든 이에게 추천!");

        memberRepository.save(member);
        productRepository.save(product);

        int count = 3;

        //when
        Long cartId = cartService.put(member.getMemberId(), product.getId(), count);

        Cart cart = cartRepository.findOne(cartId);

        //then
        assertThat(cart.getMember()).isEqualTo(member);
        assertThat(cart.getProduct()).isEqualTo(product);
        assertThat(cart.getCount()).isEqualTo(count);
    }

    @Test
    public void 장바구니_상품취소() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream("C:/Temp/hswshop/product_image/b6fbfd7d-db83-4c74-8661-175f2f03ce9b.png"));
        Product product = createProduct(productImage, "JPA", 15000, 10, "김영한님의 JPA 저서, JPA를 배우는 모든 이에게 추천!");

        memberRepository.save(member);
        productRepository.save(product);

        int count = 3;

        Long cartId = cartService.put(member.getMemberId(), product.getId(), count);

        //when
        cartService.cancel(cartId);
        Cart notExistCart = cartRepository.findOne(cartId);

        //then
        assertThat(notExistCart).isNull();
    }

    @Test
    public void 장바구니_여러개_담기() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream("C:/Temp/hswshop/product_image/b6fbfd7d-db83-4c74-8661-175f2f03ce9b.png"));
        Product product = createProduct(productImage, "JPA", 15000, 10, "김영한님의 JPA 저서, JPA를 배우는 모든 이에게 추천!");

        memberRepository.save(member);
        productRepository.save(product);

        int count = 3;

        //when
        cartService.put(member.getMemberId(), product.getId(), count);
        cartService.put(member.getMemberId(), product.getId(), count);

        List<Cart> carts = cartRepository.findAll();

        //then
        assertThat(carts.size()).isEqualTo(2);
    }

    private Member createMember(String id, String password, String name, String phone, String email, String zipcode, String address1, String address2) {
        return new MemberCreateDto(id, password, name, phone, email, zipcode, address1, address2).toEntity();
    }

    private ProductImage createProductImage(String originalName, String storeName) {
        return new ProductImageCreateDto(originalName, storeName).toEntity();
    }

    private Product createProduct(MockMultipartFile productImage, String name, int price, int stockQuantity, String description) {
        return new ProductCreateDto(productImage, name, price, stockQuantity, description).toEntity();
    }

}
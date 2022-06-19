package hsw.shop.service;

import hsw.shop.domain.Cart;
import hsw.shop.domain.Member;
import hsw.shop.domain.Product;
import hsw.shop.repository.*;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.OrderDetailDto;
import hsw.shop.web.dto.ProductCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderService orderService;

    public final String imageStore = "C:/Temp/hswshop/product_image/0d627400-0841-4062-8ac1-44120c928f4a.jpg";

    @Test
    public void 장바구니_상품담기() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream(imageStore));
        Product product = createProduct(productImage, "JPA", 15000, 10, "김영한님의 JPA 저서, JPA를 배우는 모든 이에게 추천!");

        memberRepository.save(member);
        productRepository.save(product);

        int count = 3;

        //when
        Long cartId = cartService.put(member.getMemberId(), product.getId(), count);

        Cart cart = cartRepository.findById(cartId).get();

        //then
        assertThat(cart.getMember()).isEqualTo(member);
        assertThat(cart.getProduct()).isEqualTo(product);
        assertThat(cart.getCount()).isEqualTo(count);
    }

    @Test
    public void 장바구니_상품취소() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream(imageStore));
        Product product = createProduct(productImage, "JPA", 15000, 10, "김영한님의 JPA 저서, JPA를 배우는 모든 이에게 추천!");

        memberRepository.save(member);
        productRepository.save(product);

        int count = 3;

        Long cartId = cartService.put(member.getMemberId(), product.getId(), count);

        //when
        cartService.cancel(cartId);

        //then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> {
                    cartRepository.findById(cartId).get();
                });
    }

    @Test
    public void 장바구니_여러개_담기() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream(imageStore));
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

    @Test
    public void 장바구니_주문() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream(imageStore));

        ProductCreateDto productCreateDto = ProductCreateDto.builder()
                .productImage(productImage)
                .name("JPA")
                .price(15000)
                .stockQuantity(10)
                .description("김영한님의 JPA 저서, JPA를 배우는 모든 이에게 추천!")
                .build();

        memberRepository.save(member);
        Long saveProductId = productService.saveProduct(productCreateDto);

        Product product = productRepository.findById(saveProductId).get();

        cartService.put(member.getMemberId(), product.getId(), 2);
        cartService.put(member.getMemberId(), product.getId(), 3);

        //when
        List<Long> cartIds = cartRepository.findAll().stream()
                .map(carts -> carts.getId())
                .collect(Collectors.toList());
        Long orderId = orderService.order(member.getMemberId(), cartIds);
        List<OrderDetailDto> orderDetails = orderService.orderDetailList(orderId);


        //then
        assertThat(orderDetails.size()).isEqualTo(2);
        assertThat(product.getStockQuantity()).isEqualTo(5);

    }

    private Member createMember(String id, String password, String name, String phone, String email, String zipcode, String address1, String address2) {
        return new MemberCreateDto(id, password, name, phone, email, zipcode, address1, address2).toEntity();
    }

    private Product createProduct(MockMultipartFile productImage, String name, int price, int stockQuantity, String description) {
        return new ProductCreateDto(name, price, stockQuantity, description, productImage).toEntity();
    }

}
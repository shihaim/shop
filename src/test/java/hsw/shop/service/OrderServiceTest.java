package hsw.shop.service;

import hsw.shop.domain.*;
import hsw.shop.exception.NotEnoughStockException;
import hsw.shop.domain.Member;
import hsw.shop.repository.MemberRepository;
import hsw.shop.repository.OrderRepository;
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
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Test
    public void 주문() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream("C:/Temp/hswshop/product_image/b6fbfd7d-db83-4c74-8661-175f2f03ce9b.png"));
        Product product = createProduct(productImage, "JPA", 15000, 10, "김영한님의 JPA 저서, JPA를 배우는 모든 이에게 추천!");

        memberRepository.save(member);
        productRepository.save(product);

        int orderCount = 3;

        //when
        Long orderId = orderService.order(member.getMemberId(), product.getId(), orderCount);

        Order order = orderRepository.findOne(orderId);

        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(order.getDelivery().getDeliveryStatus()).isEqualTo(DeliveryStatus.READY);
        assertThat(product.getStockQuantity()).isEqualTo(7);
        assertThat(order.getDelivery().getAddress1()).isEqualTo(member.getAddress1());
    }

    @Test
    public void 상품_재고수량초과() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream("C:/Temp/hswshop/product_image/b6fbfd7d-db83-4c74-8661-175f2f03ce9b.png"));
        Product product = createProduct(productImage, "JPA", 15000, 10, "김영한님의 JPA 저서, JPA를 배우는 모든 이에게 추천!");

        //when
        memberRepository.save(member);
        productRepository.save(product);

        int orderCount = 11;

        //then
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getMemberId(), product.getId(), orderCount);
        }, "현재 재고 수보다 주문량이 많습니다!");

    }

    @Test
    public void 주문취소() throws IOException {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        MockMultipartFile productImage = new MockMultipartFile("상품 이미지", "you.png", "image/png", new FileInputStream("C:/Temp/hswshop/product_image/b6fbfd7d-db83-4c74-8661-175f2f03ce9b.png"));
        Product product = createProduct(productImage, "JPA", 15000, 10, "김영한님의 JPA 저서, JPA를 배우는 모든 이에게 추천!");

        memberRepository.save(member);
        productRepository.save(product);

        int orderCount = 3;

        Long orderId = orderService.order(member.getMemberId(), product.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);
        Order order = orderRepository.findOne(orderId);

        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(product.getStockQuantity()).isEqualTo(10);
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
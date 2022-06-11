package hsw.shop.service;

import hsw.shop.domain.*;
import hsw.shop.domain.Member;
import hsw.shop.repository.CartRepository;
import hsw.shop.repository.MemberRepository;
import hsw.shop.repository.OrderRepository;
import hsw.shop.repository.ProductRepository;
import hsw.shop.web.dto.OrderDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    //주문
    public Long order(Long memberId, Long productId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Product product = productRepository.findOne(productId);

        //배송 정보 조회
        Delivery delivery = Delivery.createDelivery(member);

        //상세 주문 생성
        OrderDetail orderDetail = OrderDetail.createOrderDetail(product, product.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderDetail);

        orderRepository.save(order);

        return order.getId();
    }

    //장바구니 주문
    public Long order(Long memberId, List<Long> carts) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);

        //배송 정보 조회
        Delivery delivery = Delivery.createDelivery(member);

        //상세 주문 생성
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Long cartId : carts) {
            Cart cart = cartRepository.findOne(cartId);
            OrderDetail orderDetail = OrderDetail.createOrderDetail(cart.getProduct(), cart.getProduct().getPrice(), cart.getCount());
            orderDetails.add(orderDetail);
            cartRepository.remove(cart);
        }

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderDetails);

        orderRepository.save(order);

        return order.getId();
    }

    //주문 취소
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    //상세 주문 내역
    @Transactional(readOnly = true)
    public List<OrderDetailDto> orderDetailList(Long orderId) {
        List<OrderDetail> orderDetails = orderRepository.findOrderDetailsByOrderId(orderId);
        List<OrderDetailDto> result = orderDetails.stream()
                .map(od -> new OrderDetailDto(od))
                .collect(Collectors.toList());
        return result;
    }

}

package hsw.shop.service;

import hsw.shop.domain.*;
import hsw.shop.domain.Member;
import hsw.shop.repository.*;
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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다. memberId = " + memberId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다. productId = " + productId));

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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다. memberId = " + memberId));

        //배송 정보 조회
        Delivery delivery = Delivery.createDelivery(member);

        //상세 주문 생성
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Long cartId : carts) {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다. cartId = " + cartId));
            OrderDetail orderDetail = OrderDetail.createOrderDetail(cart.getProduct(), cart.getProduct().getPrice(), cart.getCount());
            orderDetails.add(orderDetail);
            cartRepository.delete(cart);
        }

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderDetails);

        orderRepository.save(order);

        return order.getId();
    }

    //주문 취소
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않은 주문입니다. orderId = " + orderId));
        order.cancel();
    }

    //상세 주문 내역
    @Transactional(readOnly = true)
    public List<OrderDetailDto> orderDetailList(Long orderId) {
        List<OrderDetail> orderDetails = orderRepository.findOrderDetailsByOrderId(orderId).orElseGet(null);

        log.info("orderDetails={}", orderDetails);

        if (orderDetails.isEmpty()) {
            log.info("order details is empty");
            return null;
        }

        List<OrderDetailDto> result = orderDetails.stream()
                .map(od -> new OrderDetailDto(od))
                .collect(Collectors.toList());

        return result;
    }

}

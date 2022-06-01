package hsw.shop.service;

import hsw.shop.domain.*;
import hsw.shop.domain.Member;
import hsw.shop.repository.MemberRepository;
import hsw.shop.repository.OrderRepository;
import hsw.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

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

    //주문 취소
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }
}

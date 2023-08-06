package hsw.shop.api.controller;

import hsw.shop.api.dto.JsonResultDto;
import hsw.shop.domain.Order;
import hsw.shop.domain.OrderDetail;
import hsw.shop.domain.OrderStatus;
import hsw.shop.repository.MemberRepository;
import hsw.shop.repository.OrderRepository;
import hsw.shop.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    /**
     * body를 x-www-form-urlencoded로 받아야하는데, json으로 받는 바람에 오류 발생함.
     */
    @PostMapping("/order")
    public ResponseEntity order(@RequestParam("memberId") Long memberId,
                                @RequestParam("productId") Long productId,
                                @RequestParam("count") int count) {
        log.info("memberId={}, productId={}, count={}", memberId, productId, count);

        Long orderId = orderService.order(memberId, productId, count);

        String memberName = memberRepository.findNameById(memberId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다. orderId = " + orderId));

        OrderResponseDto responseDto = new OrderResponseDto(memberName, order.getOrderDetails(), count, order.getStatus());

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("주문 성공!", responseDto), HttpStatus.OK);
    }

    //주문 취소
    @PostMapping("/cancel")
    public ResponseEntity cancelOrder(@RequestParam("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다. orderId = " + orderId));

        CancelResponseDto responseDto = new CancelResponseDto(order);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("주문 취소!", responseDto), HttpStatus.OK);
    }

    @Getter
    static class OrderResponseDto {

        private String memberName;
        private List<OrderDetailsResponseDto> orderDetails;
        private int count;
        private OrderStatus orderStatus;

        public OrderResponseDto(String memberName, List<OrderDetail> orderDetails, int count, OrderStatus orderStatus) {
            this.memberName = memberName;
            this.orderDetails = orderDetails.stream()
                    .map(od -> new OrderDetailsResponseDto(
                            od.getProduct().getName(),
                            od.getPrice(),
                            od.getCount()))
                    .collect(Collectors.toList());
            this.count = count;
            this.orderStatus = orderStatus;
        }
    }

    @Getter
    static class CancelResponseDto {

        private String memberName;
        private List<OrderDetailsResponseDto> orderDetails;
        private OrderStatus orderStatus;

        public CancelResponseDto(Order order) {
            this.memberName = order.getMember().getName();
            this.orderDetails = order.getOrderDetails().stream()
                    .map(od -> new OrderDetailsResponseDto(
                            od.getProduct().getName(),
                            od.getPrice(),
                            od.getCount()))
                    .collect(Collectors.toList());
            this.orderStatus = order.getStatus();
        }
    }

    @Getter
    static class OrderDetailsResponseDto {

        private String productName;
        private int price;
        private int count;

        public OrderDetailsResponseDto(String productName, int price, int count) {
            this.productName = productName;
            this.price = price;
            this.count = count;
        }
    }
}

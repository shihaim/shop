package hsw.shop.api.controller;

import hsw.shop.api.dto.JsonResultDto;
import hsw.shop.domain.Cart;
import hsw.shop.domain.Order;
import hsw.shop.repository.CartRepository;
import hsw.shop.repository.MemberRepository;
import hsw.shop.repository.OrderRepository;
import hsw.shop.repository.ProductRepository;
import hsw.shop.service.CartService;
import hsw.shop.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;
    private final OrderService orderService;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    //상품 담기
    @PostMapping("/cart-save")
    public ResponseEntity cartSave(@RequestParam("memberId") Long memberId,
                                   @RequestParam("productId") Long productId,
                                   @RequestParam("count") int count) {
        cartService.put(memberId, productId, count);

        String memberName = memberRepository.findNameById(memberId);
        String productName = productRepository.findNameById(productId);

        CartResponseDto responseDto = new CartResponseDto(memberName, productName, count);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("상품 담기 성공!", responseDto), HttpStatus.OK);
    }

    //담기 취소
    @DeleteMapping("/cart-cancel")
    public ResponseEntity cartCancel(@RequestParam("cartId") Long cartId) {
        cartService.cancel(cartId);
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니 상품입니다. cartId = " + cartId));

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("장바구니 상품 삭제!", cart), HttpStatus.OK);
    }

    /**
     * 일단 되기는 하는데, 이 방식이 맞는지 뭔가 애매함. 계속 생각해봐야 할 듯
     */
    @PostMapping("/cart-order")
    public ResponseEntity cartOrder(HttpServletRequest request) {

        AtomicReference<String> memberId = new AtomicReference<>("");
        List<Long> carts = new ArrayList<>();

        request.getParameterNames().asIterator().forEachRemaining(param -> {
            if (param.equals("memberId")) {
                memberId.set(request.getParameter(param));
            } else {
                for (String value : request.getParameterValues(param)) {
                    carts.add(Long.parseLong(value));
                }
            }
        });

        Long orderId = orderService.order(Long.parseLong(memberId.get()), carts);

        String memberName = memberRepository.findNameById(Long.parseLong(memberId.get()));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다. orderId = " + orderId));

        CartOrderResponseDto responseDto = new CartOrderResponseDto(memberName, order);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("장바구니 주문 성공!", responseDto), HttpStatus.OK);
    }

    @Getter
    static class CartResponseDto {

        private String memberName;
        private String productName;
        private int count;

        public CartResponseDto(String memberName, String productName, int count) {
            this.memberName = memberName;
            this.productName = productName;
            this.count = count;
        }
    }

    @Getter
    static class CartOrderResponseDto {
        private String memberName;
        private List<CartOrderDetailResponseDto> orderDetails;

        public CartOrderResponseDto(String memberName, Order order) {
            this.memberName = memberName;
            this.orderDetails = order.getOrderDetails().stream()
                    .map(od -> new CartOrderDetailResponseDto(
                            od.getProduct().getName(),
                            od.getPrice(),
                            od.getCount()))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class CartOrderDetailResponseDto {
        private String productName;
        private int price;
        private int count;

        public CartOrderDetailResponseDto(String productName, int price, int count) {
            this.productName = productName;
            this.price = price;
            this.count = count;
        }
    }

}

package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.domain.MemberRole;
import hsw.shop.repository.OrderRepository;
import hsw.shop.service.OrderService;
import hsw.shop.web.Login;
import hsw.shop.web.SessionConst;
import hsw.shop.web.dto.OrderDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    //주문 -> 인터셉터가 GET 메서드 못찾는다고 오류 발생함. 다른 URL 생각해야함.
    @PostMapping("/order")
    public String order(@Login Member loginMember,
                        @RequestParam("id") Long productId,
                        @RequestParam("count") int count) throws IOException {
        log.info("loginMember={}", loginMember);

        //어드민 계정일 경우 주문 막기
        if (loginMember.getRole().equals(MemberRole.ADMIN)) {
            log.info("관리자 계정으로 주문 실행 시도");
            return "redirect:/";
        }

        orderService.order(loginMember.getMemberId(), productId, count);

        return "redirect:/member/" + loginMember.getMemberId() + "/my-page";
    }

    //주문 취소 -> POST /member/{memberId}/my-page 생각해봐야 할 듯. 인터셉터가 GET 메서드 못찾는다고 오류 발생함.
    @PostMapping("/cancel")
    public String cancel(@Login Member loginMember, @RequestParam("id") Long orderId) {

        //어드민 계정일 경우 주문 취소 막기
        if (loginMember.getRole().equals(MemberRole.ADMIN)) {
            log.info("관리자 계정으로 주문 실행 시도");
            return "redirect:/";
        }

        orderService.cancelOrder(orderId);

        return "redirect:/member/" + loginMember.getMemberId() + "/my-page";
    }

    //상세 주문 내역
    @GetMapping("/member/{memberId}/my-page/{orderId}")
    public String orderDetailPage(@Login Member loginMember, @PathVariable("orderId") Long orderId, Model model) {

        List<OrderDetailDto> orderDetails = orderService.orderDetailList(orderId);
        model.addAttribute("orderDetails", orderDetails);

        int totalPrice = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 주문입니다. orderId = " + orderId))
                .getTotalOrderPrice();
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "order/OrderDetail";
    }
}

package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.domain.MemberRole;
import hsw.shop.service.OrderService;
import hsw.shop.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문
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

    //주문 취소
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
}

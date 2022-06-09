package hsw.shop.web.controller;

import hsw.shop.domain.Cart;
import hsw.shop.domain.Member;
import hsw.shop.domain.MemberRole;
import hsw.shop.repository.CartRepository;
import hsw.shop.service.CartService;
import hsw.shop.web.Login;
import hsw.shop.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final CartService cartService;

    @GetMapping("/member/{memberId}/cart")
    public String myCartPage(@Login Member loginMember, Model model) {
        List<Cart> carts = cartRepository.findAllByMemberId(loginMember.getMemberId());
        model.addAttribute("carts", carts);
        model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "member/myCart";
    }

    //장바구니 담기
    @PostMapping("/products/{productId}")
    public String put(@Login Member loginMember,
                      @RequestParam("id") Long productId,
                      @RequestParam("count") int count) {

        //어드민 계정일 경우 담기 막기
        if (loginMember.getRole().equals(MemberRole.ADMIN)) {
            log.info("관리자 계정으로 주문 실행 시도");
            return "redirect:/";
        }

        cartService.put(loginMember.getMemberId(), productId, count);

        return "redirect:/member/" + loginMember.getMemberId() + "/cart";
    }

    //장바구니 취소
    @DeleteMapping("/member/{memberId}/cart")
    public void cancel(@RequestBody Object cart) {
    }

    //장바구니 주문
    @PostMapping("/cart-order")
    public String myCartOrder(@Login Member loginMember, @RequestParam("id") List<Long> cartIds) {

        log.info("cartIds={}", cartIds);

        //어드민 계정일 경우 주문 막기
        if (loginMember.getRole().equals(MemberRole.ADMIN)) {
            log.info("관리자 계정으로 주문 실행 시도");
            return "redirect:/";
        }

        return "redirect:/member/" + loginMember.getMemberId() + "/my-page";
    }
}
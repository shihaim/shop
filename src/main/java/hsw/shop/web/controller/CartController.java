package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.domain.MemberRole;
import hsw.shop.repository.CartRepository;
import hsw.shop.service.CartService;
import hsw.shop.service.OrderService;
import hsw.shop.web.argumentresolver.Login;
import hsw.shop.web.SessionConst;
import hsw.shop.web.dto.CartIdRequestDto;
import hsw.shop.web.dto.CartListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/member/{memberId}/my-cart")
    public String myCartPage(@Login Member loginMember, @PathVariable("memberId") Long memberId, Model model) {

        //잘못된 회원 id 접근 막기
        if (loginMember.getMemberId() != memberId) {
            return "redirect:/";
        }

        List<CartListDto> carts = cartRepository.findAllByMemberId(loginMember.getMemberId()).stream()
                .map(c -> new CartListDto(c))
                .collect(Collectors.toList());
        model.addAttribute("carts", carts);
        model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "member/myCart";
    }

    //장바구니 담기
    @PostMapping("/products/{productId}")
    public String put(@Login Member loginMember,
                      @RequestParam("id") Long productId,
                      @RequestParam("count") int count) {

        if (loginMember == null) {
            return "redirect:/member/sign-in";
        }

        //어드민 계정일 경우 담기 막기
        if (loginMember.getRole().equals(MemberRole.ADMIN)) {
            log.info("관리자 계정으로 주문 실행 시도");
            return "redirect:/";
        }

        cartService.put(loginMember.getMemberId(), productId, count);

        return "redirect:/member/" + loginMember.getMemberId() + "/my-cart";
    }

    //장바구니 주문(MultiValueMap 이용해보기)
    @PostMapping("/cart-order")
    public String myCartOrder(@Login Member loginMember, @RequestParam(value = "cartIds", required = false) List<Long> cartIds) {

        //어드민 계정일 경우 주문 막기
        if (loginMember.getRole().equals(MemberRole.ADMIN)) {
            log.info("관리자 계정으로 주문 실행 시도");
            return "redirect:/";
        }

        log.info("cartIds={}", cartIds);

        if (cartIds == null) {
            return "redirect:/member/" + loginMember.getMemberId() + "/my-cart";
        }

        orderService.order(loginMember.getMemberId(), cartIds);

        return "redirect:/member/" + loginMember.getMemberId() + "/my-page";
    }

    //장바구니 취소
    @ResponseBody
    @DeleteMapping("/member/{memberId}/my-cart")
    public Long cancel(@RequestBody CartIdRequestDto requestDto) {

        cartService.cancel(requestDto.getCartId());

        return requestDto.getCartId();
    }
}

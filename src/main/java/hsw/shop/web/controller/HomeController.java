package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.repository.OrderRepository;
import hsw.shop.repository.ProductRepository;
import hsw.shop.repository.custom.ProductSearchCondition;
import hsw.shop.service.ImageStore;
import hsw.shop.web.argumentresolver.Login;
import hsw.shop.web.SessionConst;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import hsw.shop.web.dto.OrderListDto;
import hsw.shop.web.dto.ProductListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ImageStore imageStore;

    @GetMapping("/")
    public String home(@Login Member loginMember, Model model) {
        List<ProductListDto> products = productRepository.findByProductList().stream()
                .map(p -> new ProductListDto(p))
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "home";
    }

    /**
     * Get: 메서드에선 @ModelAttribute 어노테이션을 활용
     * Post: 어떤 걸 이용해도 상관 없어 보임.
     * Put, Delete: 메서드는 Form 형태로 전송이 불가능하기 때문에, JSON 형태로 전달.
     * 객체일 경우 @RequestBody, 단일 파라미터일 경우 @RequestParam을 이용
     */
    @GetMapping("/search")
    public String search(@Login Member loginMember, @ModelAttribute ProductSearchCondition condition, Model model) {
        log.info("condition={}", condition);
        List<ProductListDto> products = productRepository.searchProducts(condition);
        model.addAttribute("products", products);
        model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "home";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource viewImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + imageStore.getFullPath(filename));
    }

    @GetMapping("/member/sign-in")
    public String signInPage(Model model) {
        model.addAttribute("signInForm", new MemberSignInDto());
        return "signIn";
    }

    @GetMapping("/member/sign-up")
    public String signUpPage(Model model) {
        model.addAttribute("signUpForm", new MemberCreateDto());

        return "member/signUp";
    }

    @GetMapping("/member/{memberId}/my-page")
    public String myPage(@Login Member loginMember, @PathVariable("memberId") Long memberId, Model model) {

        //잘못된 회원 id 접근 막기
        if (loginMember.getMemberId() != memberId) {
            return "redirect:/";
        }

        List<OrderListDto> orders = orderRepository.findAllByMemberId(loginMember.getMemberId()).stream()
                .map(o -> new OrderListDto(o))
                .collect(Collectors.toList());

        model.addAttribute("orders", orders);
        model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "member/myPage";
    }
}

package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.domain.Order;
import hsw.shop.domain.Product;
import hsw.shop.repository.OrderRepository;
import hsw.shop.repository.ProductRepository;
import hsw.shop.service.ImageStore;
import hsw.shop.web.Login;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductRepository productRepository;
    private final ImageStore imageStore;
    private final OrderRepository orderRepository;

    @GetMapping("/")
    public String home(@Login Member loginMember, Model model) {

        log.info("loginMember={}", loginMember);

        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("loginMember", loginMember);

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
    public String myPage(@Login Member loginMember, Model model) {
        log.info("loginMember={}", loginMember);
        model.addAttribute("loginMember", loginMember);
        //주문 내역 보여주기
        List<Order> orders = orderRepository.findAllByMemberId(loginMember.getMemberId());
        model.addAttribute("orders", orders);
        return "member/myPage";
    }
}

package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.domain.Product;
import hsw.shop.repository.OrderRepository;
import hsw.shop.repository.ProductRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
        List<OrderListDto> orders = orderRepository.findAllByMemberId(loginMember.getMemberId()).stream()
                .map(o -> new OrderListDto(o))
                .collect(Collectors.toList());
        model.addAttribute("orders", orders);
        model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "member/myPage";
    }
}

package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.web.SessionConst;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("loginMember", loginMember);

        return "home";
    }

    @GetMapping("/sign-in")
    public String signInForm(Model model) {
        model.addAttribute("signInForm", new MemberSignInDto());
        return "signIn";
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new MemberCreateDto());

        return "member/signUp";
    }

    @GetMapping("/my-page")
    public String myPage() {
        //주문 내역 보여주기
        return "member/myPage";
    }
}

package hsw.shop.web.controller;

import hsw.shop.web.dto.MemberCreateDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/signIn")
    public String signInForm(Model model) {

        return "member/signInForm";
    }

    @GetMapping("/signUp")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new MemberCreateDto());

        return "member/signUpForm";
    }
}

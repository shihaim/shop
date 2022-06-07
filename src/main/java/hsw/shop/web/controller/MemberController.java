package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.service.MemberService;
import hsw.shop.web.SessionConst;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public String signUp(@Valid @ModelAttribute("signUpForm") MemberCreateDto memberCreateDto, BindingResult bindingResult) {

        log.info("memberCreateDto={}", memberCreateDto.toString());

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "member/signUp";
        }

        memberService.join(memberCreateDto);

        return "redirect:/";
    }

    @PostMapping("/sign-in")
    public String signIn(@Valid @ModelAttribute("signInForm") MemberSignInDto memberSignInDto,
                         BindingResult bindingResult,
                         HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.info("field errors={}", bindingResult);
            return "signIn";
        }

        Member loginMember = memberService.signIn(memberSignInDto);

        if (loginMember == null) { //field error null
            log.info("global errors={}", bindingResult);
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "signIn";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }
}

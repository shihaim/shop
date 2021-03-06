package hsw.shop.web.controller;

import hsw.shop.domain.Member;
import hsw.shop.domain.MemberRole;
import hsw.shop.service.MemberService;
import hsw.shop.web.argumentresolver.Login;
import hsw.shop.web.SessionConst;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import hsw.shop.web.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public String signUp(@Valid @ModelAttribute("signUpForm") MemberCreateDto memberCreateDto, BindingResult bindingResult) {

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
                         @RequestParam(defaultValue = "/") String redirectURL,
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

        if (loginMember.getRole().equals(MemberRole.ADMIN)) {
            log.info("login admin");
            return "redirect:/product";
        }

        log.info("redirectURL={}", redirectURL);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    /**
     * [GET,POST /member/{memberID}/my-info] error 모델을 못찾는 오류가 생김.
     * loginMember, myInfoForm을 따로 addAttribute해서 적용하니 해결
     */
    @GetMapping("/{memberId}/my-info")
    public String myInfoUpdatePage(@Login Member loginMember, @PathVariable("memberId") Long memberId, Model model) {

        //잘못된 회원 id 접근 막기
        if (loginMember.getMemberId() != memberId) {
            return "redirect:/";
        }

        model.addAttribute("myInfoForm", loginMember);
        model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "member/myInfoUpdate";
    }

    @PostMapping("/{memberId}/my-info")
    public String myInfoUpdate(@Login Member loginMember,
                               @Valid @ModelAttribute("myInfoForm") MemberUpdateDto updateDto,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            model.addAttribute(SessionConst.LOGIN_MEMBER, loginMember);

            return "member/myInfoUpdate";
        }

        memberService.updateMember(loginMember.getMemberId(), updateDto);

        return "redirect:/member/" + loginMember.getMemberId() + "/my-page";
    }
}

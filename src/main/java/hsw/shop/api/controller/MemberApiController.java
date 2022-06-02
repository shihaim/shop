package hsw.shop.api.controller;

import hsw.shop.api.dto.FieldErrorResponseDto;
import hsw.shop.api.dto.GlobalErrorResponseDto;
import hsw.shop.api.dto.JsonResultDto;
import hsw.shop.domain.Member;
import hsw.shop.service.MemberService;
import hsw.shop.web.SessionConst;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import hsw.shop.web.dto.MemberUpdateDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/member")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 지연 로딩 설정된 엔티티 값을 그대로 JSON에 받아서 넣으면
     * 직렬화하지 못하는 이슈가 발생할 수 있다.
     * 웬만하면 DTO로 반환해서 결과 값을 받아야 한다.
     * 프로젝트가 단순하니 일단 그냥 진행하자.
     */

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody MemberCreateDto memberCreateDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return new ResponseEntity<FieldErrorResponseDto>(FieldErrorResponseDto.of(bindingResult), HttpStatus.BAD_REQUEST);
        }

        Long joinMember = memberService.join(memberCreateDto);
        Member findMember = memberService.findMember(joinMember);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("회원가입 성공!", findMember), HttpStatus.OK);
    }

    //로그인은 GET보다 POST, POST보다 SSL이 좋다고 함.
    @PostMapping("/sign-in")
    public ResponseEntity signIn(@Valid @RequestBody MemberSignInDto memberSignInDto,
                                 BindingResult bindingResult,
                                 HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return new ResponseEntity<FieldErrorResponseDto>(FieldErrorResponseDto.of(bindingResult), HttpStatus.BAD_REQUEST);
        }

        Member loginMember = memberService.signIn(memberSignInDto);

        if (loginMember == null) { //field error null
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            log.info("errors={}", bindingResult);
            return new ResponseEntity<GlobalErrorResponseDto>(GlobalErrorResponseDto.of(bindingResult), HttpStatus.BAD_REQUEST);
        }

        //세션
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        Member sessionMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("로그인 성공!", sessionMember), HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            log.info("cookie name={}", cookie.getName());
            log.info("cookie value={}", cookie.getValue());
            log.info("cookie max-age={}", cookie.getMaxAge());
        }

        //세션 초기화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        HashMap<String, String> sessionMap = new HashMap<>();
        sessionMap.put("초기화된 세션ID", session.getId());

        LogoutResponseDto responseDto = new LogoutResponseDto(sessionMap);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("로그아웃 성공!", responseDto), HttpStatus.OK);
    }

    //회원 수정
    @PostMapping
    public ResponseEntity updateMember(@RequestParam("memberId") Long memberId, @Valid @RequestBody MemberUpdateDto memberUpdateDto) {

        memberService.updateMember(memberId, memberUpdateDto);
        Member member = memberService.findMember(memberId);

        MemberUpdateResponseDto responseDto = new MemberUpdateResponseDto(member);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("회원 수정 성공!", responseDto), HttpStatus.OK);
    }

    //회원 삭제
    @DeleteMapping
    public ResponseEntity removeMember(@RequestParam("memberId") Long memberId) {

        memberService.deleteMember(memberId);
        Member member = memberService.findMember(memberId);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("회원 삭제 성공!", member), HttpStatus.OK);
    }

    @Getter
    static class LogoutResponseDto {
        private Map<String, String> sessionMap;

        public LogoutResponseDto(Map<String, String> sessionMap) {
            this.sessionMap = sessionMap;
        }
    }

    @Getter
    static class MemberUpdateResponseDto {

        private String memberName;
        private String phone;
        private String email;
        private String zipcode;
        private String address1;
        private String address2;

        public MemberUpdateResponseDto(Member member) {
            this.memberName = member.getName();
            this.phone = member.getPhone();
            this.email = member.getEmail();
            this.zipcode = member.getZipcode();
            this.address1 = member.getAddress1();
            this.address2 = member.getAddress2();
        }
    }
}

package hsw.shop.api.controller;

import hsw.shop.api.dto.FieldErrorResponseDto;
import hsw.shop.api.dto.GlobalErrorResponseDto;
import hsw.shop.api.dto.JsonResultDto;
import hsw.shop.domain.Member;
import hsw.shop.service.MemberService;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

        log.info("signUp");

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return new ResponseEntity<FieldErrorResponseDto>(FieldErrorResponseDto.of(bindingResult), HttpStatus.BAD_REQUEST);
        }

        Long joinMember = memberService.join(memberCreateDto);
        Member findMember = memberService.findMember(joinMember);

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("회원가입 성공!", findMember), HttpStatus.OK);
    }

    /**
     * TODO
     * 세션도 넣어야 함.
     */
    //로그인은 GET보다 POST, POST보다 SSL이 좋다고 함.
    @PostMapping("/sign-in")
    public ResponseEntity signIn(@Valid @RequestBody MemberSignInDto memberSignInDto, BindingResult bindingResult) {

        log.info("signIn");

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

        return new ResponseEntity<JsonResultDto>(new JsonResultDto("로그인 성공!", loginMember), HttpStatus.OK);
    }

    //회원 수정

    //로그아웃

}

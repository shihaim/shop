package hsw.shop.service;

import hsw.shop.domain.Member;
import hsw.shop.domain.MemberRole;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import hsw.shop.web.dto.MemberUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void 회원가입() {
        //given
        MemberCreateDto createMember = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");

        //when
        Long savedId = memberService.join(createMember);
        Member findMember = memberService.findMember(savedId);

        //then
        assertThat(findMember.getId()).isEqualTo(createMember.getId());
    }

    @Test
    public void 회원수정() {
        //given
        MemberCreateDto createMember = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        Long memberId = memberService.join(createMember);
        Member member = memberService.findMember(memberId);

        //when
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto("hsw~", "010-5678-1234", "test@gmail.com", "22222", "인천광역시", "어딘가");
        memberService.updateMember(memberId, memberUpdateDto);
        
        //then
        assertThat(member.getName()).isEqualTo("hsw~");
    }

    @Test
    public void 회원삭제() {
        //given
        MemberCreateDto member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        Long memberId = memberService.join(member);

        //when
        memberService.deleteMember(memberId);

        //then
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> {
                    memberService.findMember(memberId);
                })
                .withMessage("존재하지 않는 회원입니다. memberId = " + memberId); //예외 메시지 검증(다르게 적으면 통과X)
    }

    @Test
    public void 로그인() {
        //given
        MemberCreateDto createMember = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");

        Long savedMemberId = memberService.join(createMember);
        Member member = memberService.findMember(savedMemberId);
        MemberSignInDto memberSignInDto = MemberSignInDto.builder()
                .id(member.getId())
                .password(member.getPassword())
                .build();

        //when
        Member loginMember = memberService.signIn(memberSignInDto);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("loginMember", loginMember);

        //then
        assertThat(mockSession.getAttribute("loginMember")).isEqualTo(loginMember); //로그인한 회원 세션에 저장되어있는지 테스트
    }

    @Test
    public void 로그아웃() {
        //given
        MemberCreateDto createMember = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");

        Long savedMemberId = memberService.join(createMember);
        Member member = memberService.findMember(savedMemberId);
        MemberSignInDto memberSignInDto = MemberSignInDto.builder()
                .id(member.getId())
                .password(member.getPassword())
                .build();

        Member loginMember = memberService.signIn(memberSignInDto);

        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("loginMember", loginMember);

        //when
        mockSession.invalidate();

        //then
        assertThat(mockSession.isInvalid()).isTrue();
    }

    private MemberCreateDto createMember(String id, String password, String name, String phone, String email, String zipcode, String address1, String address2) {
        return new MemberCreateDto(id, password, name, phone, email, zipcode, address1, address2, MemberRole.USER);
    }
}
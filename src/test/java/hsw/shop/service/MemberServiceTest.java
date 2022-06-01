package hsw.shop.service;

import hsw.shop.domain.Member;
import hsw.shop.repository.MemberRepository;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager em;

    @Test
    public void 회원가입() {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");

        //when
        Long savedId = memberService.join(member);
        Member findMember = memberService.findMember(savedId);

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void 회원수정() {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        memberService.join(member);

        //when
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto("hsw~", "010-5678-1234", "test@gmail.com", "22222", "인천광역시", "어딘가");
        memberService.updateMember(member.getMemberId(), memberUpdateDto);
        
        //then
        assertThat(member.getName()).isEqualTo("hsw~");
    }

    @Test
    public void 회원삭제() {
        //given
        Member member = createMember("test1", "1234", "테스트", "010-1234-5678", "test@naver.com", "11111", "서울시", "어딘가");
        memberService.join(member);

        //when
        memberService.deleteMember(member.getMemberId());
        Member NonExistMember = memberService.findMember(1L);

        //then
        assertThat(NonExistMember).isNull();
    }

    private Member createMember(String id, String password, String name, String phone, String email, String zipcode, String address1, String address2) {
        return new MemberCreateDto(id, password, name, phone, email, zipcode, address1, address2).toEntity();
    }
}
package hsw.shop.service;

import hsw.shop.domain.Member;
import hsw.shop.repository.MemberRepository;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import hsw.shop.web.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입
    public Long join(MemberCreateDto memberCreateDto) {
        Member member = memberCreateDto.toEntity();
        memberRepository.save(member);

        return member.getMemberId();
    }

    //로그인
    public Member signIn(MemberSignInDto memberSignInDto) {

        Optional<Member> findLoginMember = memberRepository.findAll().stream()
                .filter(m -> m.getId().equals(memberSignInDto.getId()))
                .findFirst();

        return findLoginMember.stream()
                .filter(m -> m.getPassword().equals(memberSignInDto.getPassword()))
                .findFirst()
                .orElse(null);
    }

    //회원 조회
    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다. memberId = " + memberId));
    }

    //회원 수정
    public void updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다. memberId = " + memberId))
                .update(memberUpdateDto);
    }

    //회원 삭제
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다. memberId = " + memberId));

        memberRepository.delete(member);
    }
}

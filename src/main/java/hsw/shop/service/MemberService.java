package hsw.shop.service;

import hsw.shop.domain.Member;
import hsw.shop.repository.MemberRepository;
import hsw.shop.web.dto.MemberCreateDto;
import hsw.shop.web.dto.MemberSignInDto;
import hsw.shop.web.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return memberRepository.findByLoginId(memberSignInDto.getId())
                .filter(m -> m.getPassword().equals(memberSignInDto.getPassword()))
                .orElse(null);
    }

    //회원 조회
    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    //회원 수정
    public void updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findOne(memberId);

        member.update(memberUpdateDto);
    }

    //회원 삭제
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        memberRepository.remove(member);
    }
}

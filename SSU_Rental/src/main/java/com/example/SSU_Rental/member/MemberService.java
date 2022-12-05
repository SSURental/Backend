package com.example.SSU_Rental.member;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long register(MemberRequest memberRequest){

        Optional<Member> result = memberRepository.findByLoginId(memberRequest.getLoginId());
        if (result.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
        Member member = Member.createMember(passwordEncoder, memberRequest);

        memberRepository.save(member);
        return member.getId();
    }

    public MemberResponse getOne(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 회원입니다."));


        return MemberResponse.from(member);
    }


}

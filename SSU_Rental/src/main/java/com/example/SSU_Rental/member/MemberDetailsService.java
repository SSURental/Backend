package com.example.SSU_Rental.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Spring Security 필수 메소드 구현
     *
     * @param
     * @return UserDetails
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
     */
    @Override // 기본적인 반환 타입은 UserDetails, UserDetails를 상속받은 Member로 반환 타입 지정 (자동으로 다운 캐스팅됨)
    public UserDetails loadUserByUsername(String loginId)
        throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
        Member member = memberRepository.findByLogin_id(loginId)
            .orElseThrow(() -> new UsernameNotFoundException((loginId)));

        return new MemberAuthDTO(member);
    }

    public Long save(MemberRequest memberRequest) {
        Member member = Member.makeMemberOne(passwordEncoder, memberRequest);
        memberRepository.save(member);
        return member.getMember_id();
    }
}

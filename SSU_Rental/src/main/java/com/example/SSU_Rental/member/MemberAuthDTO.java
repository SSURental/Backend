package com.example.SSU_Rental.member;

import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class MemberAuthDTO extends User {

    private Member member;

    public MemberAuthDTO(Member member) {
        super(member.getLogin_id(), member.getPw(),
            List.of(new SimpleGrantedAuthority("ROLE_" + member.getGroup().name())));
        this.member = member;
    }

}

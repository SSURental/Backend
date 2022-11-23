package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.Group;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    @Column(unique = true)
    private String login_id;

    @Column
    private String pw;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private Group group;


    @Builder
    public Member(Long member_id, String login_id, String pw, String name, Group group) {
        this.member_id = member_id;
        this.login_id = login_id;
        this.pw = pw;
        this.name = name;
        this.group = group;
    }

    public static Member makeMemberOne(PasswordEncoder passwordEncoder,
        MemberRequest memberRequest) {
        return Member.builder()
            .login_id(memberRequest.getLogin_id())
            .pw(passwordEncoder.encode(memberRequest.getPw()))
            .name(memberRequest.getName())
            .group(memberRequest.getGroup())
            .build();
    }


}

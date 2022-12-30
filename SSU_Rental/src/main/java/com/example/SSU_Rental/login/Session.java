package com.example.SSU_Rental.login;

import com.example.SSU_Rental.member.Member;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Session {

    @Id
    @GeneratedValue
    private Long id;

    private String accessToken;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Session(String accessToken, Member member) {
        this.accessToken = accessToken;
        this.member = member;
    }
}

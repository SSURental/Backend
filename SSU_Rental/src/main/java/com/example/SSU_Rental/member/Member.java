package com.example.SSU_Rental.member;
import static com.example.SSU_Rental.exception.ErrorMessage.*;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.image.MemberImage;
import com.example.SSU_Rental.login.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String loginId;

    @Column
    private String password;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private Group memberGroup;

    @OneToOne(
        cascade = {CascadeType.PERSIST,CascadeType.REMOVE},
        orphanRemoval = true,
        fetch = FetchType.LAZY,
        mappedBy = "member"
    )
    private MemberImage memberImage;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY,mappedBy = "member")
    private List<Session> sessionList = new ArrayList<>();


    @Builder
    public Member(Long id, String loginId, String password, String name, Group memberGroup) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.memberGroup = memberGroup;
    }

    public static Member createMember(MemberRequest memberRequest) {
        Member member = Member.builder()
            .loginId(memberRequest.getLoginId())
            .password(memberRequest.getPassword())
            .name(memberRequest.getName())
            .memberGroup(memberRequest.getMemberGroup())
            .build();

        MemberImage memberImage = MemberImage.builder()
            .imgName(memberRequest.getImageDTO().getImgName())
            .build();

        member.addImage(memberImage);
        return  member;
    }

    private void addImage(MemberImage memberImage) {
        this.memberImage = memberImage;
        memberImage.addMember(this);
    }

    public void modify(Member loginMember,MemberEdit memberEdit){
        if(this.id!=loginMember.getId()){
            throw new CustomException(FORBIDDEN_ERROR);
        }

        if(memberEdit.getName()!=null){
            this.name = memberEdit.getName();
        }

        if(memberEdit.getImageDTO().getImgName()!=null){
            MemberImage memberImage = MemberImage.builder()
                .imgName(memberEdit.getImageDTO().getImgName())
                .build();
            addImage(memberImage);
        }
    }


    public String addSession() {
        Session session = Session.builder()
            .accessToken(UUID.randomUUID().toString())
            .member(this)
            .build();
        sessionList.add(session);
        return session.getAccessToken();
    }
}

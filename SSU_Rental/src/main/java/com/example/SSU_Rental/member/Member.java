package com.example.SSU_Rental.member;

import static com.example.SSU_Rental.exception.ErrorMessage.FORBIDDEN_ERROR;
import static com.example.SSU_Rental.exception.ErrorMessage.UNAUTHORIZED_ERROR;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.image.MemberImage;
import com.example.SSU_Rental.login.Session;
import com.example.SSU_Rental.member.MemberEditor.MemberEditorBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY
    )
    @JoinColumn(name = "memberimage_id")
    private MemberImage memberImage;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "member")
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
            .memberGroup(memberRequest.getGroup().equals("SCHOOL")?Group.SCHOOL:Group.STUDENT)
            .build();

        MemberImage memberImage = MemberImage.builder()
            .imgName(memberRequest.getImageDTO().getImgName())
            .build();

        member.addMemberImage(memberImage);
        return member;
    }

    private void addMemberImage(MemberImage memberImage) {
        this.memberImage = memberImage;
        memberImage.addMember(this);
    }


    public MemberEditorBuilder toEditor() {
        return MemberEditor.builder()
            .name(name)
            .memberImage(memberImage);
    }

    public void edit(MemberEditor memberEditor) {
        this.name = memberEditor.getName();
        this.memberImage = memberEditor.getMemberImage();
    }


    public String addSession() {
        Session session = Session.builder()
            .accessToken(UUID.randomUUID().toString())
            .member(this)
            .build();
        sessionList.add(session);
        return session.getAccessToken();
    }

    public void validate(Member loginMember) {
        if (this.id != loginMember.getId()) {
            throw new CustomException(FORBIDDEN_ERROR);
        }
    }
}

package com.example.SSU_Rental.member;
import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.MemberImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String loginId;

    @Column
    private String pw;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private Group group;


    @OneToOne(
        cascade = {CascadeType.PERSIST,CascadeType.REMOVE},
        orphanRemoval = true,
        fetch = FetchType.LAZY,
        mappedBy = "member"
    )
    private MemberImage memberImage;


    @Builder
    public Member(Long member_id, String loginId, String pw, String name, Group group) {
        this.id = id;
        this.loginId = loginId;
        this.pw = pw;
        this.name = name;
        this.group = group;
    }

    public static Member makeMemberOne(PasswordEncoder passwordEncoder,
        MemberRequest memberRequest) {
        Member member = Member.builder()
            .loginId(memberRequest.getLogin_id())
            .pw(passwordEncoder.encode(memberRequest.getPw()))
            .name(memberRequest.getName())
            .group(memberRequest.getGroup())
            .build();

        MemberImage memberImage = MemberImage.builder()
            .imgName(memberRequest.getImageDTO().getFileName())
            .uuid(memberRequest.getImageDTO().getUuid())
            .path(memberRequest.getImageDTO().getFolderPath())
            .build();

        member.addImage(memberImage);
        return  member;
    }

    private void addImage(MemberImage memberImage) {
        this.memberImage = memberImage;
        memberImage.addMember(this);
    }


}

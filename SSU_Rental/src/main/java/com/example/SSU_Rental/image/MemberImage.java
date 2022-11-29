package com.example.SSU_Rental.image;

import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.member.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberImage {

    @Id
    @GeneratedValue
    @Column(name = "memberimage_id")
    private Long id;

    @Column(name = "memberimage_uuid")
    private String uuid;

    @Column(name = "memberimage_name")
    private String imgName;

    @Column(name = "memberimage_path")
    private String path;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public MemberImage(Long id, String uuid, String imgName, String path,
        Member member) {
        this.id = id;
        this.uuid = uuid;
        this.imgName = imgName;
        this.path = path;
        this.member = member;
    }

    public void addMember(Member member) {
        this.member = member;
    }
}

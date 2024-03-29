package com.example.SSU_Rental.image;
import com.example.SSU_Rental.member.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberImage {

    @Id
    @GeneratedValue
    @Column(name = "memberimage_id")
    private Long id;

    private String imgName;


    @Builder
    public MemberImage(String imgName) {
        this.imgName = imgName;
    }

}

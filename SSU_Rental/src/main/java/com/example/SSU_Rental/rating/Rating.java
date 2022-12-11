package com.example.SSU_Rental.rating;

import static com.example.SSU_Rental.exception.ErrorMessage.*;

import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Rating {

    @Id
    @GeneratedValue
    @Column(name = "rating_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //다대일 관계 -> 평가 여러개에 member 1
    @JoinColumn(name = "member_id")
    private Member member;  // 리뷰 남긴 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String content;

    private int score;

    @Builder
    public Rating(Long id, Member member,Item item ,String content, int score) {
        this.id = id;
        this.member = member;
        this.item = item;
        this.content = content;
        this.score = score;
    }


    public static Rating makeRatingOne(Member member,Item item ,RatingRequest ratingRequest) {
        return Rating.builder()
            .member(member)
            .content(ratingRequest.getContent())
            .item(item)
            .score(ratingRequest.getScore())
            .build();
    }

    public void validate(Member member,Item item) {

        if(this.member.getId()!=member.getId()){
            throw new CustomException(FORBIDDEN_ERROR);
        }

        if(this.item.getId()!=item.getId()){
            throw new IllegalArgumentException("아이템 아이디 혹은 리뷰 아이디가 잘못 되었습니다.");
        }

    }

//    사용하지 않은 기능 삭제
//    public void modify(RatingRequest ratingRequest) {
//        if(ratingRequest.getContent()!=null){
//            this.content = ratingRequest.getContent();
//        }
//
//        if(ratingRequest.getScore()!=0){
//            this.score = ratingRequest.getScore();
//        }
//    }
}

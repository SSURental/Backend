package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.exception.BadRequestException;
import com.example.SSU_Rental.exception.ConflictException;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.exception.notfound.RatingNotFound;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.rating.RatingEditor.RatingEditorBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

    private boolean isDeleted;

    @Builder
    public Rating(Long id, Member member,Item item ,String content, int score,boolean isDeleted) {
        this.id = id;
        this.member = member;
        this.item = item;
        this.content = content;
        this.score = score;
        this.isDeleted = isDeleted;
    }


    public static Rating createRating(Member loginMember,Item item ,RatingRequest ratingRequest) {

        if(item.getMember().getId()==loginMember.getId()){
            throw new ConflictException();
        }

        return Rating.builder()
            .member(loginMember)
            .content(ratingRequest.getContent())
            .item(item)
            .score(ratingRequest.getScore())
            .isDeleted(false)
            .build();
    }

    private void validate(Member member,Item item) {

        if(this.member.getId()!=member.getId()){
            throw new ForbiddenException();
        }

        if(this.item.getId()!=item.getId()){
            throw new RatingNotFound();
        }

    }

    public RatingEditorBuilder toEditor(){
        return RatingEditor.builder()
            .content(this.content)
            .score(this.score);
    }

    public void edit(RatingEditor editor,Member loginMember,Item item){
        validate(loginMember,item);
        this.content = editor.getContent();
        this.score = editor.getScore();
    }

    public void delete(Member loginMember,Item item){
        validate(loginMember,item);
//        if(this.isDeleted==true) throw new AlreadyDeletedException(); -> 이미 리포지터리에서 조회할 떄 삭제 여부를 검사한다.
        this.isDeleted = true;

    }
}

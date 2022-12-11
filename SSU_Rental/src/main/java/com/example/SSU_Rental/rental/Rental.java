package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.member.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "rental")
@Entity
public class Rental {

    @Id
    @GeneratedValue
    @Column(name = "rental_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //다대일 관계 -> 대여 여러개에 member 1
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)  //다대일 관계 -> 대여 한개에 item 1
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder
    public Rental(Long id, Member member, Item item, LocalDateTime startDate,
        LocalDateTime endDate) {
        this.id = id;
        this.member = member;
        this.item = item;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Rental createRental(Item item, Member member) {
        return Rental.builder()
            .member(member)
            .item(item)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(7))
            .build();
    }

    public void validate(Member member,Item item) {
        if(this.member.getId() != member.getId()){
            throw new CustomException(ErrorMessage.FORBIDDEN_ERROR);
        }

        if(this.item.getId()!=item.getId()){
            throw new IllegalArgumentException("아이템 아이디 혹은 렌탈 아이디가 잘못 되었습니다.");
        }

    }

    public void extendRental() {

        this.endDate = this.endDate.plusDays(7);

    }
}

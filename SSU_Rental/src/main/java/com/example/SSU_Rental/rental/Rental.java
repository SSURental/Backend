package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.exception.ConflictException;
import com.example.SSU_Rental.exception.ForbiddenException;
import com.example.SSU_Rental.exception.notfound.RentalNotFound;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.member.Member;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isDeleted;

    @Builder
    public Rental(Long id, Member member, Item item, LocalDate startDate,
        LocalDate endDate,boolean isDeleted) {
        this.id = id;
        this.member = member;
        this.item = item;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDeleted = isDeleted;
    }

//    public static Rental createRental(Item item, Member loginMember) {
//
//        if(item.getMember().getId()==loginMember.getId()){
//            throw new ConflictException();
//        }
//
//        item.rental(loginMember);
//
//        return Rental.builder()
//            .member(loginMember)
//            .item(item)
//            .startDate(LocalDateTime.now())
//            .endDate(LocalDateTime.now().plusDays(7))
//            .isDeleted(false)
//            .build();
//    }

    private void validate(Member member,Item item) {
        if(this.member.getId() != member.getId()){
            throw new ForbiddenException();
        }

        if(this.item.getId()!=item.getId()){
            throw new RentalNotFound();
        }

    }

    public void extendRental(Member loginMember,Item item) {
        validate(loginMember,item);
        if(LocalDate.now().isAfter(this.endDate)){
            throw new ConflictException();
        }
        this.endDate = this.endDate.plusDays(7);

    }

    public void delete(Member loginMember, Item item){
        validate(loginMember,item);
//        if(this.isDeleted==true) throw new AlreadyDeletedException(); 이미 리포지터리에서 조회할 떄 삭제 여부를 검사한다.
        this.isDeleted = true;
    }
}

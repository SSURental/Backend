package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.member.Member;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name="rental")
@Entity
public class Rental {

    @Id
    @GeneratedValue
    @Column(name = "rental_id")
    private Long rental_id;

    @Column
    @ManyToOne(fetch = FetchType.LAZY)  //다대일 관계 -> 대여 여러개에 member 1
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    @OneToOne(fetch = FetchType.LAZY)  //다대일 관계 -> 대여 한개에 item 1
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    private LocalDateTime rental_s;

    @Column
    private LocalDateTime rental_e;

}

package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.member.Member;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rental_id;

    @Column
    @ManyToOne  //다대일 관계 -> 대여 여러개에 member 1
    private Member member;

    @Column
    @OneToOne  //다대일 관계 -> 대여 한개에 item 1
    private Item item;

    @Column
    private Date rental_s;

    @Column
    private Date rental_e;

}

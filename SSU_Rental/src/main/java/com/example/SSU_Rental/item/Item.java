package com.example.SSU_Rental.item;

import com.example.SSU_Rental.enums.ItemGroup;
import com.example.SSU_Rental.enums.ItemStatus;
import com.example.SSU_Rental.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Table(name="item")
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long item_id;

    private String item_name;

    @Enumerated(EnumType.STRING)
    private ItemGroup item_group;

    @Enumerated(EnumType.STRING)
    private ItemStatus item_status;

    private int price;

    @ManyToOne //다대일 관계 -> 여러 item에 주인 한명
    private Member item_owner;

}

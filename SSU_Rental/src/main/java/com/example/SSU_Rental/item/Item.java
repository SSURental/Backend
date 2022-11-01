package com.example.SSU_Rental.item;

import com.example.SSU_Rental.enums.ItemGroup;
import com.example.SSU_Rental.enums.ItemStatus;
import com.example.SSU_Rental.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Table(name="item")
@Entity
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long item_id;

    @Column
    private String item_name;

    @Enumerated(EnumType.STRING)
    @Column
    private ItemGroup item_group;

    @Enumerated(EnumType.STRING)
    @Column
    private ItemStatus item_status;

    @Column
    private int price;

    @Column
    @ManyToOne //다대일 관계 -> 여러 item에 주인 한명
    @JoinColumn(name = "member_id")
    private Member item_owner;

}

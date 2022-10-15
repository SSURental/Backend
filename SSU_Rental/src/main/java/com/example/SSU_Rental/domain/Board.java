package com.example.SSU_Rental.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;


    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Board(Long id, String title, String content, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.member = member;
    }
}

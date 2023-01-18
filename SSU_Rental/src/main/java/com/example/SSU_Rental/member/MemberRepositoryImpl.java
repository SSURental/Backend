package com.example.SSU_Rental.member;

import static com.example.SSU_Rental.image.QMemberImage.*;
import static com.example.SSU_Rental.member.QMember.*;

import com.example.SSU_Rental.image.QMemberImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    //연관관계를 다 끌어와야 할때는 get~() 사용, 연관관계 필요없는 때는 findById 사용
    @Override
    public Member getMember(Long memberId) {
        return jpaQueryFactory.selectFrom(member)
            .leftJoin(member.memberImage, memberImage).fetchJoin()
            .where(member.id.eq(memberId))
            .fetchOne();
    }
}

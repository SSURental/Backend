package com.example.SSU_Rental.member;

import static com.example.SSU_Rental.image.QMemberImage.memberImage;
import static com.example.SSU_Rental.member.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    //연관관계를 다 끌어와야 할때는 get~() 사용, 연관관계 필요없는 때는 findById 사용
    @Override
    public Optional<Member> getMember(Long memberId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(member)
            .leftJoin(member.memberImage, memberImage).fetchJoin()
            .where(member.id.eq(memberId))
            .fetchOne());
    }
}

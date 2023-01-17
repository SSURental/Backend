package com.example.SSU_Rental.rental;

import static com.example.SSU_Rental.item.QItem.item;
import static com.example.SSU_Rental.rental.QRental.rental;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.QMember;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class RentalRepositoryImpl implements RentalRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Rental> getList(Member member, RequestPageDTO requestPageDTO) {

        List<Rental> content = jpaQueryFactory.selectFrom(rental)
            .leftJoin(rental.member, QMember.member).fetchJoin()
            .leftJoin(rental.item, item).fetchJoin()
            .where(rental.member.eq(member).and(rental.isDeleted.eq(false)))
            .orderBy(rental.id.desc())
            .offset(requestPageDTO.getOffset())
            .limit(requestPageDTO.getSize())
            .fetch();

        JPAQuery<Long> total = jpaQueryFactory.select(rental.count())
            .from(rental)
            .where(rental.member.eq(member));

        return PageableExecutionUtils.getPage(content, requestPageDTO.getPageable(),
            total::fetchOne);

    }

}

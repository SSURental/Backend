package com.example.SSU_Rental.rating;

import static com.example.SSU_Rental.item.QItem.item;
import static com.example.SSU_Rental.member.QMember.member;
import static com.example.SSU_Rental.rating.QRating.rating;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.QItem;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.QMember;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class RatingRepositoryImpl implements RatingRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Rating> getList(Item item, RequestPageDTO requestPageDTO) {
        List<Rating> content = jpaQueryFactory.selectFrom(rating)
            .leftJoin(rating.member, member).fetchJoin()
            .leftJoin(rating.item, QItem.item).fetchJoin()
            .where(rating.item.eq(item).and(rating.isDeleted.eq(false)))
            .orderBy(rating.id.desc())
            .offset(requestPageDTO.getOffset())
            .limit(requestPageDTO.getSize())
            .fetch();

        JPAQuery<Long> total = jpaQueryFactory.select(rating.count())
            .from(rating)
            .where(rating.item.eq(item).and(rating.isDeleted.eq(false)));

        return PageableExecutionUtils.getPage(content, requestPageDTO.getPageable(),
            total::fetchOne);
    }


    @Override
    public Page<Rating> getMyRatingList(Member member, RequestPageDTO requestPageDTO) {
        List<Rating> content = jpaQueryFactory.selectFrom(rating)
            .leftJoin(rating.member, QMember.member).fetchJoin()
            .leftJoin(rating.item, item).fetchJoin()
            .where(rating.member.eq(member).and(rating.isDeleted.eq(false)))
            .orderBy(rating.id.desc())
            .offset(requestPageDTO.getOffset())
            .limit(requestPageDTO.getSize())
            .fetch();

        JPAQuery<Long> total = jpaQueryFactory.select(rating.count())
            .from(rating)
            .where(rating.member.eq(member).and(rating.isDeleted.eq(false)));
        return PageableExecutionUtils.getPage(content, requestPageDTO.getPageable(),
            total::fetchOne);
    }


    //연관관계를 다 끌어와야 할때는 get~() 사용, 연관관계 필요없는 때는 findById 사용
    @Override
    public Optional<Rating> getRating(Long ratingId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(rating)
            .leftJoin(rating.member, QMember.member).fetchJoin()
            .leftJoin(rating.item, item).fetchJoin()
            .where(rating.id.eq(ratingId).and(rating.isDeleted.eq(false)))
            .fetchOne());
    }
}

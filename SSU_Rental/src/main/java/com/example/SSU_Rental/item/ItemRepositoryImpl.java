package com.example.SSU_Rental.item;

import static com.example.SSU_Rental.item.ItemStatus.AVAILABLE;
import static com.example.SSU_Rental.item.QItem.item;
import static com.example.SSU_Rental.member.QMember.member;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.QMember;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Object[]> getList(RequestPageDTO requestPageDTO) {
        List<Item> tuples = jpaQueryFactory.select(item)
            .from(item)
            .leftJoin(item.member, member).fetchJoin()
            .distinct()
            .where(item.status.eq(AVAILABLE).and(item.itemGroup.eq(
                requestPageDTO.getGroup().equals("SCHOOL") ? Group.SCHOOL : Group.STUDENT)))
            .orderBy(item.id.desc())
            .offset(requestPageDTO.getOffset())
            .limit(requestPageDTO.getSize())
            .fetch();

        List<Object[]> content = tuples.stream().map(item1 -> {
            Object[] objects = new Object[2];
            objects[0] = item1;
            objects[1] = item1.getItemImages().get(0);
            return objects;
        }).collect(Collectors.toList());

        JPAQuery<Long> total = jpaQueryFactory.select(item.count())
            .from(item)
            .where(item.status.eq(AVAILABLE).and(item.itemGroup.eq(
                requestPageDTO.getGroup().equals("SCHOOL") ? Group.SCHOOL : Group.STUDENT)));

        return PageableExecutionUtils.getPage(content, requestPageDTO.getPageable(),
            total::fetchOne);
    }

    @Override
    public Page<Object[]> getMyItemList(Member member, RequestPageDTO requestPageDTO) {
        List<Item> tuples = jpaQueryFactory.select(item)
            .from(item)
            .leftJoin(item.member, QMember.member).fetchJoin()
            .distinct()
            .where(item.member.eq(member))
            .orderBy(item.id.desc())
            .offset(requestPageDTO.getOffset())
            .limit(requestPageDTO.getSize())
            .fetch();

        List<Object[]> content = tuples.stream().map(item1 -> {
            Object[] objects = new Object[2];
            objects[0] = item1;
            objects[1] = item1.getItemImages().get(0);
            return objects;
        }).collect(Collectors.toList());

        JPAQuery<Long> total = jpaQueryFactory.select(item.count())
            .from(item)
            .where(item.member.eq(member));

        return PageableExecutionUtils.getPage(content, requestPageDTO.getPageable(),
            total::fetchOne);
    }

    @Override
    public List<Object[]> getItem(Long itemId) {

        Item item = jpaQueryFactory.select(QItem.item)
            .from(QItem.item)
            .leftJoin(QItem.item.member, member).fetchJoin()
            .distinct()
            .where(QItem.item.id.eq(itemId))
            .fetchOne();

        Object[] objects = new Object[2];
        objects[0] = item;
        objects[1] = item.getItemImages();
        List<Object[]> content = new ArrayList<>();
        content.add(objects);

        return content;
    }
}

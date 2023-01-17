package com.example.SSU_Rental.board;

import static com.example.SSU_Rental.board.QBoard.board;
import static com.example.SSU_Rental.member.QMember.member;

import com.example.SSU_Rental.common.RequestPageDTO;
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
public class BoardRepositoryImpl implements BoardRepositoryCustom {


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Board> getList(RequestPageDTO requestPageDTO) {
        List<Board> content = jpaQueryFactory.selectFrom(board)
            .leftJoin(board.member, member).fetchJoin()
            .where(board.blocked.eq(false).and(board.isDeleted.eq(false)))
            .orderBy(board.id.desc())
            .limit(requestPageDTO.getSize())
            .offset(requestPageDTO.getOffset())
            .fetch();

        JPAQuery<Long> total = jpaQueryFactory.select(board.count())
            .from(board)
            .where(board.blocked.eq(false).and(board.isDeleted.eq(false)));

        return PageableExecutionUtils.getPage(content, requestPageDTO.getPageable(),
            total::fetchOne);
    }


    @Override
    public Page<Board> getMyBoardList(Member member, RequestPageDTO requestPageDTO) {
        List<Board> content = jpaQueryFactory.selectFrom(board)
            .leftJoin(board.member, QMember.member).fetchJoin()
            .where(board.member.eq(member).and(board.isDeleted.eq(false)))
            .orderBy(board.id.desc())
            .offset(requestPageDTO.getOffset())
            .limit(requestPageDTO.getSize())
            .fetch();

        JPAQuery<Long> total = jpaQueryFactory.select(board.count())
            .from(board)
            .where(board.member.eq(member));

        return PageableExecutionUtils.getPage(content, requestPageDTO.getPageable(),
            total::fetchOne);
    }
}

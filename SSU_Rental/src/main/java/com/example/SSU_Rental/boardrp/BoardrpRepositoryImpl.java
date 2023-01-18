package com.example.SSU_Rental.boardrp;

import static com.example.SSU_Rental.boardrp.QBoardrp.boardrp;
import static com.example.SSU_Rental.member.QMember.member;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.QBoard;
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
public class BoardrpRepositoryImpl implements BoardrpRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Boardrp> getList(Board board, RequestPageDTO requestPageDTO) {
        List<Boardrp> content = jpaQueryFactory.selectFrom(boardrp)
            .leftJoin(boardrp.member, member).fetchJoin()
            .leftJoin(boardrp.board, QBoard.board).fetchJoin()
            .where(boardrp.board.eq(board).and(boardrp.isDeleted.eq(false)))
            .orderBy(boardrp.id.desc())
            .offset(requestPageDTO.getOffset())
            .limit(requestPageDTO.getSize())
            .fetch();

        JPAQuery<Long> total = jpaQueryFactory.select(boardrp.count())
            .from(boardrp)
            .where(boardrp.board.eq(board));

        return PageableExecutionUtils.getPage(content, requestPageDTO.getPageable(),
            total::fetchOne);
    }


    @Override
    public Page<Boardrp> getMyReplyList(Member member, RequestPageDTO requestPageDTO) {
        List<Boardrp> content = jpaQueryFactory.selectFrom(boardrp)
            .leftJoin(boardrp.member, QMember.member).fetchJoin()
            .leftJoin(boardrp.board, QBoard.board).fetchJoin()
            .where(boardrp.member.eq(member).and(boardrp.isDeleted.eq(false)))
            .orderBy(boardrp.id.desc())
            .offset(requestPageDTO.getOffset())
            .limit(requestPageDTO.getSize())
            .fetch();

        JPAQuery<Long> total = jpaQueryFactory.select(boardrp.count())
            .from(boardrp)
            .where(boardrp.member.eq(member));

        return PageableExecutionUtils.getPage(content, requestPageDTO.getPageable(),
            total::fetchOne);
    }

    //연관관계를 다 끌어와야 할때는 get~() 사용, 연관관계 필요없는 때는 findById 사용
    @Override
    public Boardrp getBoardrp(Long boardrpId) {
        return jpaQueryFactory.selectFrom(boardrp)
            .leftJoin(boardrp.member, QMember.member).fetchJoin()
            .leftJoin(boardrp.board, QBoard.board).fetchJoin()
            .where(boardrp.id.eq(boardrpId).and(boardrp.isDeleted.eq(false)))
            .fetchOne();
    }
}

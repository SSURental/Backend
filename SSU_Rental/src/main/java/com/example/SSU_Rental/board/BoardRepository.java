package com.example.SSU_Rental.board;

import com.example.SSU_Rental.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {


    @EntityGraph(attributePaths = "member", type = EntityGraphType.LOAD)
    @Query("select b from Board b where b.blocked = false")
    Page<Board> getListPage(Pageable pageable);




    @EntityGraph(attributePaths = "member", type = EntityGraphType.LOAD)
    Page<Board> findByMember(Member member,Pageable pageable);
}

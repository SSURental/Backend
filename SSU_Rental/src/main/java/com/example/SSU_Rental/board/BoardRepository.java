package com.example.SSU_Rental.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = "member",type = EntityGraphType.LOAD)
    @Query("select b from Board b order by b.id desc ")
    Page<Board> getListPage(Pageable pageable);
}

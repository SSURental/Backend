package com.example.SSU_Rental.boardrp;


import com.example.SSU_Rental.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardrpRepository extends JpaRepository<Boardrp, Long> {

    @EntityGraph(attributePaths = {"board", "member"}, type = EntityGraphType.LOAD)
    Page<Boardrp> findByBoard(Board board, Pageable pageable);
}

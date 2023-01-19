package com.example.SSU_Rental.board;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>,BoardRepositoryCustom {

    @EntityGraph
    @Override
    Optional<Board> findById(Long aLong);

    Optional<Board> findBy
}

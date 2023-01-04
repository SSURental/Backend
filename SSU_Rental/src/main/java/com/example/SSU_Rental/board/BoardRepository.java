package com.example.SSU_Rental.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>,BoardRepositoryCustom {

}

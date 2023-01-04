package com.example.SSU_Rental.boardrp;


import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardrpRepository extends JpaRepository<Boardrp, Long>,BoardrpRepositoryCustom {
}

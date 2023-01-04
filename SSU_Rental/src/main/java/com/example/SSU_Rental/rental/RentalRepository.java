package com.example.SSU_Rental.rental;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long>,RentalRepositoryCustom {
}

package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @EntityGraph(attributePaths = {"item","member"})
    Page<Rental> findByMember(Member member, Pageable pageable);
}

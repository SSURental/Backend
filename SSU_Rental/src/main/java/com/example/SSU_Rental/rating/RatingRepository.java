package com.example.SSU_Rental.rating;


import com.example.SSU_Rental.member.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select avg(r.score) from Rating r where r.member =:member")
    Optional<Double> findByMemberForAvg(@Param("member") Member member);

    Page<Rating> findByMember(Member member, Pageable pageable);
}

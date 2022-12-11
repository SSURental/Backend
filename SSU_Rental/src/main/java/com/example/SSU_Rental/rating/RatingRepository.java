package com.example.SSU_Rental.rating;


import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.member.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("select avg(r.score) from Rating r where r.item =:item")
    Optional<Double> findByItemForAvg(@Param("item") Item item);

    @EntityGraph(attributePaths = {"member","item"},type = EntityGraphType.LOAD)
    Page<Rating> findByItem(Item item, Pageable pageable);

    @EntityGraph(attributePaths = {"member","item"},type = EntityGraphType.LOAD)
    Page<Rating> findByMember(Member member,Pageable pageable);
}

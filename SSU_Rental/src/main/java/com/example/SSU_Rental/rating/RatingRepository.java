package com.example.SSU_Rental.rating;


import com.example.SSU_Rental.item.Item;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long>,RatingRepositoryCustom {

    @Query("select avg(r.score) from Rating r where r.item =:item")
    Optional<Double> findByItemForAvg(@Param("item") Item item);
}

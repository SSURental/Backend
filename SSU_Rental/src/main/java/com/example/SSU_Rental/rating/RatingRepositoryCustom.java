package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.member.Member;
import org.springframework.data.domain.Page;

public interface RatingRepositoryCustom  {


    Page<Rating> getList(Item item, RequestPageDTO requestPageDTO);

    Page<Rating> getMyRatingList(Member member,RequestPageDTO requestPageDTO);

    Rating getRating(Long ratingId);
}

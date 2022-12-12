package com.example.SSU_Rental.rating;

import static com.example.SSU_Rental.exception.ErrorMessage.*;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardResponse;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {

    private final MemberRepository memberRepository;
    private final RatingRepository ratingRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long register(Long itemId, RatingRequest ratingRequest,Long memberId) {

        Member member = getMember(memberId);
        Item item = getItem(itemId);

        Rating rating = Rating.makeRatingOne(member,item,ratingRequest);
        ratingRepository.save(rating);
        return rating.getId();
    }

    public Double getAvgScores(Long itemId) {
        Item item = getItem(itemId);

        return ratingRepository.findByItemForAvg(item).orElseThrow(()->new IllegalArgumentException("점수가 없습니다."));
    }

    public ResponsePageDTO getRentalList(Long itemId, RequestPageDTO requestPageDTO) {

        Item item = getItem(itemId);

        Page<Rating> resultPage = ratingRepository.findByItem(item, requestPageDTO.getPageable());

        Function<Rating, RatingResponse> fn = (entity -> RatingResponse.from(entity));
        return new ResponsePageDTO(resultPage, fn);
    }

//    사용하지 않은 기능 삭제
//    @Transactional
//    public void modify(Long itemId,Long ratingId, Long memberId,RatingRequest ratingRequest) {
//        Item item = getItem(itemId);
//        Member member = getMember(memberId);
//        Rating rating = getRating(ratingId);
//        rating.validate(member,item);
//        rating.modify(ratingRequest);
//        return;
//    }


    @Transactional
    public void remove(Long itemId,Long ratingId, Long memberId) {
        Item item = getItem(itemId);
        Member member = getMember(memberId);
        Rating rating = getRating(ratingId);
        rating.validate(member,item);
        ratingRepository.delete(rating);

    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND_ERROR));
    }

    private Item getItem(Long itemId){
        return itemRepository.findById(itemId)
            .orElseThrow(()->new CustomException(ITEM_NOT_FOUND_ERROR));
    }

    private Rating getRating(Long ratingId){
        return ratingRepository.findById(ratingId)
            .orElseThrow(()->new CustomException(RATING_NOT_FOUND_ERROR));
    }



}

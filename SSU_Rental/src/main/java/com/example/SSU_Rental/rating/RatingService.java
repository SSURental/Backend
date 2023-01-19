package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.AlreadyDeletedException;
import com.example.SSU_Rental.exception.notfound.ItemNotFound;
import com.example.SSU_Rental.exception.notfound.MemberNotFound;
import com.example.SSU_Rental.exception.notfound.RatingNotFound;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.rating.RatingEditor.RatingEditorBuilder;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Long register(Long itemId, RatingRequest ratingRequest, UserSession session) {

        Member loginMember = getMember(session.getId());
        Item item = getItem(itemId);
        Rating rating = Rating.createRating(loginMember, item, ratingRequest);
        ratingRepository.save(rating);
        return rating.getId();
    }

    public Double getAvgScores(Long itemId) {
        Item item = getItem(itemId);

        return ratingRepository.findByItemForAvg(item)
            .orElseThrow(() -> new RatingNotFound());
    }

    public ResponsePageDTO getList(Long itemId, RequestPageDTO requestPageDTO) {

        Item item = getItem(itemId);
        Page<Rating> resultPage = ratingRepository.getList(item,requestPageDTO);
        Function<Rating, RatingResponse> fn = (entity -> RatingResponse.from(entity));
        return new ResponsePageDTO(resultPage, fn);
    }


    @Transactional
    public void edit(Long itemId, Long ratingId, RatingEdit editRequest,
        UserSession session) {
        Item item = getItem(itemId);
        Member loginMember = getMember(session.getId());
        Rating rating = getRating(ratingId);
        RatingEditorBuilder ratingEditorBuilder = rating.toEditor();
        RatingEditor editor = ratingEditorBuilder.content(editRequest.getContent())
            .score(editRequest.getScore())
            .build();
        rating.edit(editor,loginMember,item);
        return;
    }


    @Transactional
    public void delete(Long itemId, Long ratingId, UserSession session) {
        Item item = getItem(itemId);
        Member loginMember = getMember(session.getId());
        Rating rating = getRating(ratingId);
        rating.delete(loginMember, item);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFound());
    }

    private Item getItem(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFound());
        if(findItem.isDeleted()) throw new AlreadyDeletedException();
        return findItem;
    }

    private Rating getRating(Long ratingId) {
        Rating findRating = ratingRepository.findById(ratingId)
            .orElseThrow(() -> new RatingNotFound());
        if(findRating.isDeleted()) throw new AlreadyDeletedException();
        return findRating;
    }


}

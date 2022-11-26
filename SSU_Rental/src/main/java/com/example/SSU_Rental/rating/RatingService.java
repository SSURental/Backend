package com.example.SSU_Rental.rating;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardResponse;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
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

    @Transactional
    public Long register(Long member_id, RatingRequest ratingRequest) {

        Member member = getMember(member_id);

        Rating rating = Rating.makeRatingOne(member, ratingRequest);
        ratingRepository.save(rating);
        return rating.getRating_id();
    }

    public Integer getAvgScores(Long member_id) {
        Member member = getMember(member_id);

        Optional<Integer> avgScore = ratingRepository.findByMemberForAvg(member);
        return avgScore.orElseThrow(() -> new IllegalArgumentException("없는 점수입니다."));
    }

    public ResponsePageDTO getRatingList(Long member_id, RequestPageDTO requestPageDTO) {

        Member member = getMember(member_id);

        Pageable pageRequest = PageRequest.of(requestPageDTO.getPage() - 1,
            requestPageDTO.getSize());

        Page<Rating> resultPage = ratingRepository.findByMember(member, pageRequest);

        Function<Rating, RatingResponse> fn = (entity -> RatingResponse.from(entity));
        return new ResponsePageDTO(resultPage, fn);
    }


    public Member getMember(Long member_id) {

        return memberRepository.findById(member_id)
            .orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));
    }

}

package com.example.SSU_Rental.member;

import static com.example.SSU_Rental.exception.ErrorMessage.*;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.board.BoardResponse;
import com.example.SSU_Rental.boardrp.Boardrp;
import com.example.SSU_Rental.boardrp.BoardrpRepository;
import com.example.SSU_Rental.boardrp.BoardrpResponse;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.image.ItemImage;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.item.ItemResponse;
import com.example.SSU_Rental.rating.Rating;
import com.example.SSU_Rental.rating.RatingRepository;
import com.example.SSU_Rental.rating.RatingResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardRepository boardRepository;
    private final BoardrpRepository boardrpRepository;
    private final ItemRepository itemRepository;
    private final RatingRepository ratingRepository;

    @Transactional
    public Long register(MemberRequest memberRequest){

        Optional<Member> result = memberRepository.findByLoginId(memberRequest.getLoginId());
        if (result.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
        Member member = Member.createMember(passwordEncoder, memberRequest);

        memberRepository.save(member);
        return member.getId();
    }

    public MemberResponse getOne(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 회원입니다."));


        return MemberResponse.from(member);
    }


    @Transactional
    public void modify(Long memberId, MemberEdit memberEdit,
        Long loginMemberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));

        Member loginMember = memberRepository.findById(loginMemberId)
            .orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));

        member.modify(loginMember,memberEdit);
        return;
    }

    public ResponsePageDTO getMyItem(RequestPageDTO requestPageDTO, Long memberId) {

        Member member = getMember(memberId);
        Page<Object[]> resultPage = itemRepository.findByMember(member, requestPageDTO.getPageable());
        Function<Object[], ItemResponse> fn = (obj -> ItemResponse.from((Item) obj[0],(List<ItemImage>)(Arrays.asList((ItemImage)obj[1]))));
        return new ResponsePageDTO(resultPage,fn);


    }


    public ResponsePageDTO getMyReply(RequestPageDTO requestPageDTO, Long memberId) {

        Member member = getMember(memberId);

        Page<Boardrp> resultPage = boardrpRepository.findByMember(member, requestPageDTO.getPageable());

        Function<Boardrp, BoardrpResponse> fn = (entity->BoardrpResponse.from(entity));

        return new ResponsePageDTO(resultPage,fn);



    }

    public ResponsePageDTO getMyBoard(RequestPageDTO requestPageDTO, Long memberId) {
        Member member = getMember(memberId);
        Page<Board> resultPage = boardRepository.findByMember(member, requestPageDTO.getPageable());

        Function<Board, BoardResponse> fn = (entity->BoardResponse.from(entity));

        return new ResponsePageDTO(resultPage,fn);


    }


    public ResponsePageDTO getMyRating(RequestPageDTO requestPageDTO, Long memberId) {
        Member member = getMember(memberId);
        Page<Rating> resultPage = ratingRepository.findByMember(member, requestPageDTO.getPageable());
        Function<Rating, RatingResponse> fn = (entity->RatingResponse.from(entity));
        return new ResponsePageDTO(resultPage,fn);

    }


    private Member getMember(Long memberId){
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND_ERROR));
    }



}

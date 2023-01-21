package com.example.SSU_Rental.member;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.board.BoardResponse;
import com.example.SSU_Rental.boardrp.Boardrp;
import com.example.SSU_Rental.boardrp.BoardrpRepository;
import com.example.SSU_Rental.boardrp.BoardrpResponse;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.BadRequestException;
import com.example.SSU_Rental.exception.notfound.MemberNotFound;
import com.example.SSU_Rental.image.MemberImage;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.item.ItemResponse;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.MemberEditor.MemberEditorBuilder;
import com.example.SSU_Rental.rating.Rating;
import com.example.SSU_Rental.rating.RatingRepository;
import com.example.SSU_Rental.rating.RatingResponse;
import com.example.SSU_Rental.rental.Rental;
import com.example.SSU_Rental.rental.RentalRepository;
import com.example.SSU_Rental.rental.RentalResponse;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardrpRepository boardrpRepository;
    private final ItemRepository itemRepository;
    private final RatingRepository ratingRepository;
    private final RentalRepository rentalRepository;

    @Transactional
    public Long register(MemberRequest memberRequest) {

        Optional<Member> result = memberRepository.findByLoginId(memberRequest.getLoginId());
        if (result.isPresent()) {
            throw new BadRequestException();
        }
        Member member = Member.createMember(memberRequest);

        memberRepository.save(member);
        return member.getId();
    }

    public MemberResponse getOne(Long memberId) {
        Member member = memberRepository.getMember(memberId).orElseThrow(()->new MemberNotFound());
        return MemberResponse.from(member);
    }


    @Transactional
    public void edit(Long memberId, MemberEdit memberEdit, UserSession session) {
        Member member = memberRepository.getMember(memberId).orElseThrow(()-> new MemberNotFound());
        Member loginMember = getMember(session.getId());

        MemberEditorBuilder memberEditorBuilder = member.toEditor();
        MemberImage memberImage = new MemberImage(memberEdit.getImageDTO().getImgName());
        MemberEditor memberEditor = memberEditorBuilder.name(memberEdit.getName())
            .memberImage(memberImage)
            .build();
        member.edit(memberEditor,loginMember);
        return;
    }

    public ResponsePageDTO getMyItemList(RequestPageDTO requestPageDTO, UserSession session) {

        Member member = getMember(session.getId());
        Page<Item> resultPage = itemRepository.getMyItemList(member, requestPageDTO);
        Function<Item, ItemResponse> fn =(item -> ItemResponse.from(item));
        return new ResponsePageDTO(resultPage, fn);


    }


    public ResponsePageDTO getMyReplyList(RequestPageDTO requestPageDTO, UserSession session) {

        Member member = getMember(session.getId());
        Page<Boardrp> resultPage = boardrpRepository.getMyReplyList(member, requestPageDTO);
        Function<Boardrp, BoardrpResponse> fn = (entity -> BoardrpResponse.from(entity));
        return new ResponsePageDTO(resultPage, fn);
    }

    public ResponsePageDTO getMyBoardList(RequestPageDTO requestPageDTO, UserSession session) {
        Member member = getMember(session.getId());
        Page<Board> resultPage = boardRepository.getMyBoardList(member, requestPageDTO);
        Function<Board, BoardResponse> fn = (entity -> BoardResponse.from(entity));

        return new ResponsePageDTO(resultPage, fn);


    }


    public ResponsePageDTO getMyRatingList(RequestPageDTO requestPageDTO, UserSession session) {
        Member member = getMember(session.getId());
        Page<Rating> resultPage = ratingRepository.getMyRatingList(member, requestPageDTO);
        Function<Rating, RatingResponse> fn = (entity -> RatingResponse.from(entity));
        return new ResponsePageDTO(resultPage, fn);

    }

    public ResponsePageDTO getMyRentalList(RequestPageDTO requestPageDTO, UserSession session) {
        Member member = getMember(session.getId());
        Page<Rental> myRentalList = rentalRepository.getList(member, requestPageDTO);
        Function<Rental, RentalResponse> fn = (rental -> RentalResponse.from(rental));
        return new ResponsePageDTO(myRentalList, fn);
    }


    // 간단한 조회일때는 findById, 연관관계 다 끌고 와야 할때는 Itemrepository.getItem();
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFound());
    }


}

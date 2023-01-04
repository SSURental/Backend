package com.example.SSU_Rental.rental;

import static com.example.SSU_Rental.exception.ErrorMessage.ITEM_NOT_FOUND_ERROR;
import static com.example.SSU_Rental.exception.ErrorMessage.MEMBER_NOT_FOUND_ERROR;
import static com.example.SSU_Rental.exception.ErrorMessage.RENTAL_NOT_FOUND_ERROR;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.login.UserSession;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentalService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final RentalRepository rentalRepository;

    @Transactional
    public Long rental(Long itemId, UserSession session) {

        Item item = getItem(itemId);

        Member member = getMember(session.getId());

        item.rental(member);

        Rental rental = Rental.createRental(item, member);
        rentalRepository.save(rental);
        return rental.getId();

    }

    public RentalResponse getOne(Long itemId, Long rentalId) {
        Rental rental = getRental(rentalId);
        return RentalResponse.from(rental);
    }

    public ResponsePageDTO getList(RequestPageDTO requestPageDTO, UserSession session) {

        Member member = getMember(session.getId());
        Page<Rental> myRentalList = rentalRepository.getList(member, requestPageDTO);
        Function<Rental, RentalResponse> fn = (rental -> RentalResponse.from(rental));
        return new ResponsePageDTO(myRentalList, fn);
    }

    @Transactional
    public RentalResponse extendRental(Long itemId, Long rentalId, UserSession session) {

        Item item = getItem(itemId);
        Rental rental = getRental(rentalId);
        Member member = getMember(session.getId());
        rental.validate(member, item);
        rental.extendRental();
        return RentalResponse.from(rental);
    }


    @Transactional
    public void returnItem(Long itemId, Long rentalId, UserSession session) {
        Rental rental = getRental(rentalId);
        Member member = getMember(session.getId());
        Item item = getItem(itemId);
        rental.validate(member, item);
        rentalRepository.delete(rental);
        item.returnItem();
    }

    private Item getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new CustomException(ITEM_NOT_FOUND_ERROR));
        return item;
    }

    private Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException((MEMBER_NOT_FOUND_ERROR)));
        return member;
    }

    private Rental getRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new CustomException((RENTAL_NOT_FOUND_ERROR)));
        return rental;
    }


}

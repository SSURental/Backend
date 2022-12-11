package com.example.SSU_Rental.rental;

import static com.example.SSU_Rental.exception.ErrorMessage.*;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.CustomException;
import com.example.SSU_Rental.exception.ErrorMessage;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Long rental(Long itemId, Long memberId) {

        Item item = getItem(itemId);

        Member member = getMember(memberId);

        item.rental(member);

        Rental rental = Rental.createRental(item, member);
        rentalRepository.save(rental);
        return rental.getId();

    }

    public RentalResponse getOne(Long rentalId) {
        Rental rental = getRental(rentalId);
        return RentalResponse.from(rental);
    }

    public ResponsePageDTO getMyRentalList(Long memberId, RequestPageDTO requestPageDTO) {

        Member member = getMember(memberId);

        Page<Rental> myRentalList = rentalRepository.findByMember(member, requestPageDTO.getPageable());

        Function<Rental, RentalResponse> fn = (rental -> RentalResponse.from(rental));

        return new ResponsePageDTO(myRentalList, fn);
    }

    @Transactional
    public RentalResponse extendRental(Long itemId,Long rentalId, Long memberId) {

        Item item = getItem(itemId);
        Rental rental = getRental(rentalId);
        Member member = getMember(memberId);
        rental.validate(member,item);
        rental.extendRental();
        return RentalResponse.from(rental);
    }


    @Transactional
    public void returnItem(Long itemId,Long rentalId, Long memberId) {
        Rental rental = getRental(rentalId);
        Member member = getMember(memberId);
        Item item = rental.getItem();
        rental.validate(member,item);
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

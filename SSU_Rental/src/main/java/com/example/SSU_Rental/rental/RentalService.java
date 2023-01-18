package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.exception.AlreadyDeletedException;
import com.example.SSU_Rental.exception.notfound.ItemNotFound;
import com.example.SSU_Rental.exception.notfound.MemberNotFound;
import com.example.SSU_Rental.exception.notfound.RentalNotFound;
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
        Member loginMember = getMember(session.getId());
        Rental rental = item.rental(loginMember);
        rentalRepository.save(rental);
        return rental.getId();

    }

    public RentalResponse getOne(Long itemId, Long rentalId) {
        Rental rental = getRental(rentalId);
        return RentalResponse.from(rental);
    }

    public ResponsePageDTO getList(RequestPageDTO requestPageDTO, UserSession session) {

        Member loginMember = getMember(session.getId());
        Page<Rental> myRentalList = rentalRepository.getList(loginMember, requestPageDTO);
        Function<Rental, RentalResponse> fn = (rental -> RentalResponse.from(rental));
        return new ResponsePageDTO(myRentalList, fn);
    }

    @Transactional
    public RentalResponse extendRental(Long itemId, Long rentalId, UserSession session) {

        Item item = getItem(itemId);
        Rental rental = getRental(rentalId);
        Member loginMember = getMember(session.getId());
        rental.extendRental(loginMember,item);
        return RentalResponse.from(rental);
    }


    @Transactional
    public void returnItem(Long itemId, Long rentalId, UserSession session) {
        Rental rental = getRental(rentalId);
        Member loginMember = getMember(session.getId());
        Item item = getItem(itemId);
        rental.delete(loginMember,item);
        item.returnItem();
    }

    private Item getItem(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFound());
        if(findItem.isDeleted()) throw new AlreadyDeletedException();
        return findItem;
    }

    private Member getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFound());
        return member;
    }

    private Rental getRental(Long rentalId) {
        Rental findRental = rentalRepository.findById(rentalId)
            .orElseThrow(() -> new RentalNotFound());
        if(findRental.isDeleted()) throw new AlreadyDeletedException();
        return findRental;
    }


}

package com.example.SSU_Rental.rental;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.member.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface RentalRepositoryCustom  {

    Page<Rental> getList(Member member, RequestPageDTO requestPageDTO);

    Optional<Rental> getRental(Long rentalId);

}

package com.example.SSU_Rental.member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> getMember(Long memberId);

}

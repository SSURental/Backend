package com.example.SSU_Rental.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //email을 통해 회원을 조회하기 위해 findByEmail만듬
    Optional<Member> findByLoginId(String Login_id);
}

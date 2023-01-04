package com.example.SSU_Rental.login;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session,Long> {

    Optional<Session> findByAccessToken(String accessToken);

}

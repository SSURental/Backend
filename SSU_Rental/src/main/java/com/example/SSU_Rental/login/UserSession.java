package com.example.SSU_Rental.login;


import lombok.Getter;

@Getter
public class UserSession {

    private Long id;

    public UserSession(Long id) {
        this.id = id;
    }
}

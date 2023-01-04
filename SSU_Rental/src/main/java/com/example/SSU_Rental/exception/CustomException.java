package com.example.SSU_Rental.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorResponseDTO errorResponseDTO;


    public CustomException(ErrorMessage e){
        super(e.getMessage());
        this.errorResponseDTO = ErrorResponseDTO.of(e);
    }

}

package com.example.SSU_Rental.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseDTO {

    private int code;
    private HttpStatus status;
    private String message;

    private ErrorResponseDTO(ErrorMessage e){
        this.message = e.getMessage();
        this.status = e.getStatus();
        this.code = e.getCode();
    }

    private ErrorResponseDTO(ErrorMessage e, String message){
        this(e);
        this.message = message;
    }

    public static ErrorResponseDTO of(ErrorMessage e){
        return new ErrorResponseDTO(e);
    }

    public static ErrorResponseDTO of(ErrorMessage e,String message){
        return new ErrorResponseDTO(e,message);
    }




}

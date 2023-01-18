package com.example.SSU_Rental.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {



    @ExceptionHandler(SsuRentalException.class)
    public ResponseEntity<ErrorResponse> handleSsuRentalException(SsuRentalException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
            .code(String.valueOf(statusCode))
            .message(e.getMessage())
            .validation(e.getValidation())
            .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
            .body(body);

        return response;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e){
        e.printStackTrace();
        ErrorResponse body = ErrorResponse.builder()
            .code("400")
            .message("잘못된 요청입니다.")
            .validation(null)
            .build();


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(BAD_REQUEST)
            .body(body);

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        ErrorResponse body = ErrorResponse.builder()
            .code("400")
            .message("잘못된 요청입니다.")
            .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            body.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(BAD_REQUEST)
            .body(body);

        return response;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e){
        e.printStackTrace();
        ErrorResponse body = ErrorResponse.builder()
            .code("500")
            .message("서버 오류 입니다.")
            .validation(null)
            .build();


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(BAD_REQUEST)
            .body(body);

        return response;
    }




}

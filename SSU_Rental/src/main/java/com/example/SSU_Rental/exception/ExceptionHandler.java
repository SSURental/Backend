package com.example.SSU_Rental.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {



    @org.springframework.web.bind.annotation.ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<ErrorResponse> handleBadRequestException(final IllegalArgumentException e){
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

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException e){
        ErrorResponse body = ErrorResponse.builder()
            .code("400")
            .message("잘못된 요청입니다.")
            .validation(null)
            .build();


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(BAD_REQUEST)
            .body(body);

        return response;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception e){
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

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(SsuRentalException.class)
    public ResponseEntity<ErrorResponse> SsuRentalException(SsuRentalException e) {
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




}

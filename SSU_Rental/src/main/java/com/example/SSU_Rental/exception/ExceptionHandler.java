package com.example.SSU_Rental.exception;

import static com.example.SSU_Rental.exception.ErrorMessage.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    protected ResponseEntity<ErrorResponseDTO> handleCustomException(final CustomException e){
        e.printStackTrace();
        return buildResponseEntity(e.getErrorResponseDTO());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<ErrorResponseDTO> handleBadRequestException(final IllegalArgumentException e){
        e.printStackTrace();
        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.of(BAD_REQUEST_ERROR, e.getMessage());
        return buildResponseEntity(errorResponseDTO);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDTO> handleException(final Exception e){
        e.printStackTrace();
        return buildResponseEntity(ErrorResponseDTO.of(INTERNAL_SERVER_ERROR));
    }




    private ResponseEntity<ErrorResponseDTO> buildResponseEntity(ErrorResponseDTO errorResponseDTO){
        return ResponseEntity.status(errorResponseDTO.getStatus()).body(errorResponseDTO);
    }

}

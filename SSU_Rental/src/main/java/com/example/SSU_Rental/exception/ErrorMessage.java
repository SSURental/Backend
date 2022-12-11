package com.example.SSU_Rental.exception;

import static org.springframework.http.HttpStatus.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {


    /**
     * 400번대 예외
     *
     */

    //Bad Request
    BAD_REQUEST_ERROR(400, BAD_REQUEST,"잘못된 요청입니다."),
    FORBIDDEN_ERROR(403, FORBIDDEN,"해당 컨텐츠에 접근할 권한이 없습니다"),
    MEMBER_NOT_FOUND_ERROR(404, NOT_FOUND,"해당 멤버는 존재하지 않습니다."),
    ITEM_NOT_FOUND_ERROR(404, NOT_FOUND,"해당 아이템은 존재하지 않습니다."),
    BOARD_NOT_FOUND_ERROR(404,NOT_FOUND,"해당 게시글은 존재하지 않습니다."),
    REPLY_NOT_FOUND_ERROR(404,NOT_FOUND,"해당 댓글은 존재하지 않습니다."),
    RATING_NOT_FOUND_ERROR(404,NOT_FOUND,"해당 리뷰는 존재하지 않습니다."),
    RENTAL_NOT_FOUND_ERROR(404,NOT_FOUND,"해당 렌털은 존재하지 않습니다."),



    /**
     * 500번대 예외
     */

    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    ;


    private final int code;
    private final HttpStatus status;
    private final String message;

}

package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.login.UserSession;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Long> register(
        @Validated @RequestBody MemberRequest memberRequest) { // 회원 추가

        Long registerId = memberService.register(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + registerId)).body(registerId);
    }

    @GetMapping("/members/{memberId}")
    private ResponseEntity<MemberResponse> getOne(
        @PathVariable Long memberId) {
        MemberResponse response = memberService.getOne(memberId);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 멤버 정보 수정 -> 이름, 속한 그룹, 이미지만 변경 가능
     */

    @PatchMapping("/members/{memberId}")
    private ResponseEntity<Long> modify(
        @PathVariable Long memberId,
        @RequestBody MemberEdit memberEdit, UserSession session) {
        memberService.modify(memberId, memberEdit, session);
        return ResponseEntity.ok().body(memberId);
    }

    /**
     * 여기서 부터 내가 올린 아이템 목록, 댓글 목록, 게시글 목록, 렌탈 목록, 평가 목록 확인 가능
     */

    @GetMapping("/members/items")
    private ResponseEntity<ResponsePageDTO> getMyItem(
        RequestPageDTO requestPageDTO,
        UserSession session) {
        ResponsePageDTO responsePage = memberService.getMyItemList(requestPageDTO, session);

        return ResponseEntity.ok().body(responsePage);
    }


    @GetMapping("/members/replys")
    private ResponseEntity<ResponsePageDTO> getMyReply(RequestPageDTO requestPageDTO,
        UserSession session) {
        ResponsePageDTO responsePage = memberService.getMyReplyList(requestPageDTO,
            session);

        return ResponseEntity.ok().body(responsePage);
    }


    @GetMapping("/members/boards")
    private ResponseEntity<ResponsePageDTO> getMyBoard(RequestPageDTO requestPageDTO,
        UserSession session) {
        ResponsePageDTO responsePage = memberService.getMyBoardList(requestPageDTO,
            session);

        return ResponseEntity.ok().body(responsePage);
    }

    @GetMapping("/members/ratings")
    private ResponseEntity<ResponsePageDTO> getMyRating(
        RequestPageDTO requestPageDTO,
        UserSession session) {
        ResponsePageDTO responsePage = memberService.getMyRatingList(requestPageDTO,
            session);
        return ResponseEntity.ok().body(responsePage);
    }


    @GetMapping("/members/rentals")
    private ResponseEntity<ResponsePageDTO> getMyRental(RequestPageDTO requestPageDTO,
        UserSession session) {
        ResponsePageDTO responsePage = memberService.getMyRentalList(requestPageDTO,
            session);
        return ResponseEntity.ok().body(responsePage);
    }

}
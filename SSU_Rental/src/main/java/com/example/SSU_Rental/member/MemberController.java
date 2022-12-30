package com.example.SSU_Rental.member;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
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
        @RequestBody MemberEdit memberEdit, @AuthMember Member member) {
        memberService.modify(memberId, memberEdit, member.getId());
        return ResponseEntity.ok().body(memberId);
    }

    @GetMapping("/members/items")
    private ResponseEntity<ResponsePageDTO> getMyItem(
        RequestPageDTO requestPageDTO,
        @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyItemList(requestPageDTO, member.getId());

        return ResponseEntity.ok().body(responsePage);
    }


    /**
     * 내가(로그인한 객체) 쓴 댓글 모음
     *
     * @param requestPageDTO
     * @param member
     * @return
     */

    @GetMapping("/members/replys")
    private ResponseEntity<ResponsePageDTO> getMyReply(RequestPageDTO requestPageDTO,
        @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyReplyList(requestPageDTO, member.getId());

        return ResponseEntity.ok().body(responsePage);
    }


    /**
     * 내가(로그인한 객체) 쓴 게시글 모음
     *
     * @param requestPageDTO
     * @param member
     * @return
     */


    @GetMapping("/members/boards")
    private ResponseEntity<ResponsePageDTO> getMyBoard(RequestPageDTO requestPageDTO,
        @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyBoardList(requestPageDTO, member.getId());

        return ResponseEntity.ok().body(responsePage);
    }

    @GetMapping("/members/ratings")
    private ResponseEntity<ResponsePageDTO> getMyRating(
        RequestPageDTO requestPageDTO,
        @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyRatingList(requestPageDTO,
            member.getId());
        return ResponseEntity.ok().body(responsePage);
    }


    @GetMapping("/members/rentals")
    private ResponseEntity<ResponsePageDTO> getMyRental(RequestPageDTO requestPageDTO,
        @AuthMember Member member) {
        ResponsePageDTO responsePage = memberService.getMyRentalList(requestPageDTO,
            member.getId());
        return ResponseEntity.ok().body(responsePage);
    }

}
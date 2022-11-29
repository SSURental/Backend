package com.example.SSU_Rental.boardrp;

import com.example.SSU_Rental.member.AuthMember;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardrpController {


    private final BoardrpService boardrpService;


    @PostMapping("/board/{board_id}/reply")
    public ResponseEntity<Long> register(@PathVariable Long board_id,
        @RequestBody BoardrpRequest boardrpRequest, @AuthMember Member member) {
        Long registerId = boardrpService.register(board_id, boardrpRequest, member.getId());
        return ResponseEntity.created(URI.create("/board/" + board_id + "/reply" + registerId))
            .body(registerId);
    }

    @GetMapping("/board/{board_id}/reply")
    public ResponseEntity<ResponsePageDTO> getList(@PathVariable Long board_id,
        RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = boardrpService.getList(board_id, requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }

    @PatchMapping("/board/{board_id}/reply/{reply_id}")
    public ResponseEntity<Long> modify(@PathVariable Long board_id, @PathVariable Long reply_id,
        @RequestBody BoardrpRequest request, @AuthMember Member member) {

        boardrpService.modify(board_id, reply_id, request, member.getId());
        return ResponseEntity.ok().body(reply_id);

    }

    @DeleteMapping("/board/{board_id}/reply/{reply_id}")
    public ResponseEntity delete(@PathVariable Long board_id, @PathVariable Long reply_id,
        @AuthMember Member member) {

        boardrpService.delete(board_id, reply_id, member.getId());
        return ResponseEntity.ok().build();
    }
}

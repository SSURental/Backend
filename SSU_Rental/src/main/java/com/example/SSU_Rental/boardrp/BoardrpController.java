package com.example.SSU_Rental.boardrp;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.login.UserSession;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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


    @PostMapping("/boards/{boardId}/replys")
    public ResponseEntity<Long> register(@PathVariable Long boardId,
        @Validated @RequestBody BoardrpRequest boardrpRequest,
        UserSession session) {
        Long registerId = boardrpService.register(boardId, boardrpRequest, session);
        return ResponseEntity.created(URI.create("/board/" + boardId + "/reply" + registerId))
            .body(registerId);
    }

    @GetMapping("/boards/{boardId}/replys")
    public ResponseEntity<ResponsePageDTO> getList(@PathVariable Long boardId,
        RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = boardrpService.getReplyList(boardId, requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }


    @PatchMapping("/boards/{boardId}/replys/{replyId}")
    public ResponseEntity<Long> modify(@PathVariable Long boardId, @PathVariable Long replyId,
        @RequestBody BoardrpRequest request, UserSession session) {

        boardrpService.modify(boardId, replyId, request, session);
        return ResponseEntity.ok().body(replyId);

    }

    @DeleteMapping("/boards/{boardId}/replys/{replyId}")
    public ResponseEntity delete(
        @PathVariable Long boardId,
        @PathVariable Long replyId,
        UserSession session) {

        boardrpService.delete(boardId, replyId, session);
        return ResponseEntity.ok().build();
    }
}

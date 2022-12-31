package com.example.SSU_Rental.board;

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
public class BoardController {

    private final BoardService boardService;


    @PostMapping("/boards")
    public ResponseEntity<Long> register(@Validated @RequestBody BoardRequest boardRequest,
        UserSession session) {

        Long registerId = boardService.register(boardRequest, session);
        return ResponseEntity.created(URI.create("/board/" + registerId)).body(registerId);

    }


    @PostMapping("/boards/{boardId}/like")
    public ResponseEntity<Long> like(@PathVariable Long boardId, UserSession session) {
        boardService.like(boardId);
        return ResponseEntity.ok().body(boardId);
    }


    @PostMapping("/boards/{boardId}/dislike")
    public ResponseEntity<Long> dislike(
        @PathVariable Long boardId, UserSession session) {
        boardService.dislike(boardId);
        return ResponseEntity.ok().body(boardId);
    }

    @PostMapping("/boards/{boardId}/warn")
    public ResponseEntity<Long> warn(
        @PathVariable Long boardId, UserSession session) {
        boardService.warn(boardId);
        return ResponseEntity.ok().body(boardId);
    }


    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponse> getOne(@PathVariable Long boardId) {

        return ResponseEntity.ok().body(boardService.getOne(boardId));
    }

    /**
     * /boards ? page =1& size=5& sort=id -> 최신 순대로 정렬 /boards ? page =1& size=5& sort=see_cnt ->
     * 조회수 대로 정렬
     *
     * @param requestPageDTO
     * @return
     */


    @GetMapping("/boards")
    public ResponseEntity<ResponsePageDTO> getList(RequestPageDTO requestPageDTO) {
        ResponsePageDTO responsePage = boardService.getBoardList(requestPageDTO);

        return ResponseEntity.ok().body(responsePage);
    }


    @PatchMapping("/boards/{boardId}")
    public ResponseEntity<Long> edit(@PathVariable Long boardId,
        @RequestBody BoardEdit editRequest, UserSession session) {

        boardService.edit(boardId, editRequest, session);
        return ResponseEntity.ok().body(boardId);
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity delete(
        @PathVariable Long boardId,
        UserSession session) {
        boardService.delete(boardId, session);
        return ResponseEntity.ok().build();
    }
}

package com.example.SSU_Rental.board;

import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/board")
    public ResponseEntity<Long> register(@RequestBody BoardRequest boardRequest,@AuthenticationPrincipal Member member){

        Long registerId = boardService.register(boardRequest, member);
        return ResponseEntity.created(URI.create("/board/"+registerId)).body(registerId);

    }

    @PostMapping("/board/{board_id}/recommend")
    public ResponseEntity<Long> recommend(@PathVariable Long board_id){
        boardService.recommend(board_id);
        return ResponseEntity.ok().body(board_id);
    }

    @PostMapping("/board/{board_id}/warn")
    public ResponseEntity<Long> warn(@PathVariable Long board_id){
        boardService.warn(board_id);
        return ResponseEntity.ok().body(board_id);
    }

    @GetMapping("/board/{board_id}")
    public ResponseEntity<BoardResponse> getOne(@PathVariable Long board_id){

        return ResponseEntity.ok().body(boardService.getOne(board_id));
    }


    @GetMapping("/board")
    public ResponseEntity<ResponsePageDTO> getList(RequestPageDTO requestPageDTO){
        ResponsePageDTO response = boardService.getList(requestPageDTO);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/board/{board_id}")
    public ResponseEntity<Long> modify(@PathVariable Long board_id, @RequestBody BoardRequest boardRequest,@AuthenticationPrincipal Member member){

        boardService.modify(board_id,boardRequest,member);
        return ResponseEntity.ok().body(board_id);
    }

    @DeleteMapping("/board/{board_id}")
    public ResponseEntity delete(@PathVariable Long board_id,@AuthenticationPrincipal Member member){
        boardService.delete(board_id,member);
        return ResponseEntity.ok().build();
    }
}

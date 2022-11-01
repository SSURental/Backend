package com.example.SSU_Rental.board;

import com.example.SSU_Rental.PageResponse;
import com.example.SSU_Rental.RequestPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


    @PostMapping("/board")
    public ResponseEntity<Long> register(@Validated @RequestBody BoardRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.register(request));

    }


    @PostMapping("/board/{board_id}/recommend")
    public ResponseEntity<Long> recommend(@PathVariable Long board_id,@RequestBody Long member_id){
        boardService.recommend(board_id,member_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(board_id);
    }

    @PostMapping("/board/{board_id}/warn")
    public ResponseEntity<Long> warn(@PathVariable Long board_id,@RequestBody Long member_id){
        boardService.warn(board_id,member_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(board_id);
    }

    @GetMapping("/board/{board_id}")
    public ResponseEntity<BoardResponse> getOne(@PathVariable Long board_id){

        return ResponseEntity.ok().body(boardService.getOne(board_id));
    }

    @GetMapping("/board")
    public ResponseEntity<PageResponse> getList(RequestPageDTO requestPageDTO){
        PageResponse response = boardService.getList(requestPageDTO);

        return ResponseEntity.ok().body(response);
    }



    @PatchMapping("/board/{board_id}")
    public ResponseEntity<Long> modify(@PathVariable Long board_id, @RequestBody BoardRequest boardRequest){

        boardService.modify(board_id,boardRequest);
        return ResponseEntity.status(HttpStatus.OK).body(board_id);
    }

    @DeleteMapping("/board/{board_id}")
    public ResponseEntity delete(@PathVariable Long board_id,@RequestBody Long member_id){
        boardService.delete(board_id,member_id);
        return ResponseEntity.ok().build();
    }





}

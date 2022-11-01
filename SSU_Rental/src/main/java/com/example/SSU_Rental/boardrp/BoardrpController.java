package com.example.SSU_Rental.boardrp;

import com.example.SSU_Rental.PageResponse;
import com.example.SSU_Rental.RequestPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Long> register(@PathVariable Long board_id, @RequestBody BoardrpRequest request){
         return ResponseEntity.status(HttpStatus.CREATED).body(boardrpService.register(board_id,request));
    }

    @GetMapping("/board/{board_id}/reply")
    public ResponseEntity<PageResponse> getList(@PathVariable Long board_id,RequestPageDTO requestPageDTO){
        return ResponseEntity.ok().body(boardrpService.getList(board_id,requestPageDTO));
    }

    @PatchMapping("/board/{board_id}/reply/{reply_id}")
    public ResponseEntity<Long> modify(@PathVariable Long board_id, @PathVariable Long reply_id, @RequestBody BoardrpRequest request){

        boardrpService.modify(board_id,reply_id,request);
        return ResponseEntity.ok().body(reply_id);

    }
    @DeleteMapping("/board/{board_id}/reply/{reply_id}")
    public ResponseEntity delete(@PathVariable Long board_id,@PathVariable Long reply_id,@RequestBody Long member_id){

        boardrpService.delete(board_id,reply_id,member_id);
        return ResponseEntity.ok().build();
    }



}

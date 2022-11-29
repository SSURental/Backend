package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.member.AuthMember;
import com.example.SSU_Rental.member.Member;
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
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/items")
    public ResponseEntity<Long> register(@RequestBody ItemRequest itemRequest,
        @AuthMember Member member) {

        Long registerId = itemService.register(itemRequest, member.getId());
        return ResponseEntity.created(URI.create("/item/" + registerId)).body(registerId);

    }

    @GetMapping("/items")
    public ResponseEntity<ResponsePageDTO> getItemList(@RequestBody RequestPageDTO requestPageDTO) {

        ResponsePageDTO responsePage = itemService.getItemList(requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }

    @GetMapping("/items/{item_id}")
    public ResponseEntity<ItemResponse> getOne(@PathVariable Long item_id){
        ItemResponse itemResponse = itemService.getOne(item_id);

        return ResponseEntity.ok(itemResponse);
    }

    @PatchMapping("/items/{item_id}")
    public ResponseEntity<Long> modify(@PathVariable Long item_id,
        @RequestBody ItemRequest itemRequest, @AuthMember Member member) {

        itemService.modify(item_id, itemRequest, member.getId());
        return ResponseEntity.ok().body(item_id);

    }

    @DeleteMapping("/items/{item_id}")
    public ResponseEntity delete(@PathVariable Long item_id, @AuthMember Member member) {
        itemService.delete(item_id, member.getId());
        return ResponseEntity.ok().build();
    }


}

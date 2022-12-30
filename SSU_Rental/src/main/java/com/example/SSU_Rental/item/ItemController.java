package com.example.SSU_Rental.item;

import com.example.SSU_Rental.common.RequestPageDTO;
import com.example.SSU_Rental.common.ResponsePageDTO;
import com.example.SSU_Rental.member.AuthMember;
import com.example.SSU_Rental.member.Member;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @PostMapping("/items")
    public ResponseEntity<Long> register(@Validated @RequestBody ItemRequest itemRequest,
        @AuthMember Member member) {

        Long registerId = itemService.register(itemRequest, member.getId());
        return ResponseEntity.created(URI.create("/item/" + registerId)).body(registerId);

    }


    @GetMapping("/items")
    public ResponseEntity<ResponsePageDTO> getItemList(
        RequestPageDTO requestPageDTO) {

        ResponsePageDTO responsePage = itemService.getItemList(requestPageDTO);
        return ResponseEntity.ok().body(responsePage);
    }


    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemResponse> getOne(
        @PathVariable Long itemId) {
        ItemResponse itemResponse = itemService.getOne(itemId);

        return ResponseEntity.ok(itemResponse);
    }


    @DeleteMapping("/items/{itemId}")
    public ResponseEntity delete(
        @PathVariable Long itemId,
        @AuthMember Member member) {
        itemService.delete(itemId, member.getId());
        return ResponseEntity.ok().build();
    }


}

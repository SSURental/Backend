package com.example.SSU_Rental.rental;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.item.ItemRequest;
import com.example.SSU_Rental.login.LoginDTO;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    @DisplayName("렌탈 등록")
    public void test1() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        //Act
        mockMvc.perform(post("/items/{itemId}/rentals",item.getId()).cookie(cookie))
            //Assert
            .andExpect(status().isCreated())
            .andDo(print());

    }


    @Test
    @DisplayName("렌탈 한개 조회")
    public void test2() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rental rental = createRental(anotherMember, item);
        rentalRepository.save(rental);

        //Act
        mockMvc.perform(get("/items/{itemId}/rentals/{rentalId}",item.getId(),rental.getId()))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.itemId").value(item.getId()))
            .andExpect(jsonPath("$.itemName").value("아이템"))
            .andExpect(jsonPath("$.nickname").value("유저2"))
            .andExpect(jsonPath("$.startDate").value(LocalDate.now().toString()))
            .andExpect(jsonPath("$.endDate").value(LocalDate.now().plusDays(7).toString()))
            .andExpect(jsonPath("$.imageDTO.imgName").value("item-img01"))
            .andDo(print());

    }

    @Test
    @DisplayName("내가 한 렌탈 목록 보기")
    public void test3() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        List<Item> items = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> createItem("아이템" + i, i * 1000,"img"+i,"image"+i ,member)).collect(
                Collectors.toList());
        itemRepository.saveAll(items);
        List<Rental> rentals = new ArrayList<>();
        for (Item item : items) {
            rentals.add(createRental(anotherMember,item));
        }
        rentalRepository.saveAll(rentals);

        //Act
        mockMvc.perform(get("/rentals?page=1&size=5").cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(5)))
            .andExpect(jsonPath("$.hasNext", is(true)))
            .andExpect(jsonPath("$.contents[0].itemId").value(items.get(20).getId()))
            .andExpect(jsonPath("$.contents[0].itemName").value("아이템21"))
            .andExpect(jsonPath("$.contents[0].nickname").value("유저2"))
            .andExpect(jsonPath("$.contents[0].startDate").value(LocalDate.now().toString()))
            .andExpect(jsonPath("$.contents[0].endDate").value(LocalDate.now().plusDays(7).toString()))
            .andExpect(jsonPath("$.contents[0].imageDTO.imgName").value("img21"))
            .andDo(print());
    }

    @Test
    @DisplayName("렌탈 기한 연장")
    public void test4() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rental rental = createRental(anotherMember, item);
        rentalRepository.save(rental);

        //Act
        mockMvc.perform(patch("/items/{itemId}/rentals/{rentalId}",item.getId(),rental.getId()).cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.itemId").value(item.getId()))
            .andExpect(jsonPath("$.itemName").value("아이템"))
            .andExpect(jsonPath("$.nickname").value("유저2"))
            .andExpect(jsonPath("$.startDate").value(LocalDate.now().toString()))
            .andExpect(jsonPath("$.endDate").value(LocalDate.now().plusDays(14).toString()))
            .andExpect(jsonPath("$.imageDTO.imgName").value("item-img01"))
            .andDo(print());
    }

    @Test
    @DisplayName("렌탈 반납")
    public void test5() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Rental rental = createRental(anotherMember, item);
        rentalRepository.save(rental);
        itemRepository.save(item);

        //Act
        mockMvc.perform(delete("/items/{itemId}/rentals/{rentalId}",item.getId(),rental.getId()).cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andDo(print());
    }

    private Member createMember(String loginId, String password, String name, String group,
        String imgName) {
        return Member.createMember(
            MemberRequest.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .group(group)
                .imageDTO(new ImageDTO(imgName))
                .build());
    }

    private Cookie createCookie(Member member) throws Exception {
        LoginDTO loginDTO = new LoginDTO(member.getLoginId(), member.getPassword());
        String json = objectMapper.writeValueAsString(loginDTO);

        return mockMvc.perform(
                post("/login").contentType(APPLICATION_JSON).content(json))
            .andDo(print())
            .andReturn().getResponse().getCookie("SESSION");
    }

    private Item createItem(String itemName,int price,String imgName1,String imgName2,Member member){
        ItemRequest itemRequest = new ItemRequest(itemName, price);
        itemRequest.getImageDTOList().add(new ImageDTO(imgName1));
        itemRequest.getImageDTOList().add(new ImageDTO(imgName2));
        return Item.createItem(itemRequest,member);
    }

    private Rental createRental(Member loginMember, Item item){
        return item.rental(loginMember);
    }
}
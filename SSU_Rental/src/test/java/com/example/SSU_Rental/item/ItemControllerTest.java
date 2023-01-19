package com.example.SSU_Rental.item;

import static com.example.SSU_Rental.common.Group.*;
import static com.example.SSU_Rental.item.ItemStatus.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.login.LoginDTO;
import com.example.SSU_Rental.member.Member;
import com.example.SSU_Rental.member.MemberRepository;
import com.example.SSU_Rental.member.MemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void clean() {
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("아이템 등록")
    public void test1() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        ItemRequest itemRequest = new ItemRequest("아이템", 10000);
        String json = objectMapper.writeValueAsString(itemRequest);

        //Act
        mockMvc.perform(post("/items").contentType(APPLICATION_JSON).content(json).cookie(cookie))
            //Assert
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    @DisplayName("아이템 등록할 때 아이템 이름은 공백이 될 수 없고, 가격은 최소 1000이상 이어야 합니다. ")
    public void test2() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        ItemRequest itemRequest = new ItemRequest("", 500);
        String json = objectMapper.writeValueAsString(itemRequest);

        //Act
        mockMvc.perform(post("/items").contentType(APPLICATION_JSON).content(json).cookie(cookie))
            //Assert
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is("400")))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.itemName").value("아이템 이름은 공백이 될 수 없습니다."))
            .andExpect(jsonPath("$.validation.price").value("최소 가격은 1000원 이상이어야 합니다."))
            .andDo(print());
    }

    @Test
    @DisplayName("아이템 1개 조회 ")
    public void test3() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저", "STUDENT", "img01");
        memberRepository.save(member);
        Item item = createItem("아이템", 10000, "img01", "img02", member);
        itemRepository.save(item);

        //Act
        mockMvc.perform(get("/items/{itemId}", item.getId()))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(item.getId()))
            .andExpect(jsonPath("$.itemName").value("아이템"))
//            .andExpect(jsonPath("$.group").value(STUDENT))
//            .andExpect(jsonPath("$.status").value(AVAILABLE))
            .andExpect(jsonPath("$.memberName").value("유저"))
            .andExpect(jsonPath("$.price").value(10000))
            .andExpect(jsonPath("$.imageDTOList[0].imgName").value("img01"))
            .andExpect(jsonPath("$.imageDTOList[1].imgName").value("img02"))
            .andDo(print());
    }

    @Test
    @DisplayName("아이템 목록 페이지 조회 ")
    public void test4() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저", "STUDENT", "img01");
        memberRepository.save(member);
        List<Item> items = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> createItem("아이템" + i, i * 1000, "img" + i, "image" + i, member)).collect(
                Collectors.toList());
        itemRepository.saveAll(items);

        //Act
        mockMvc.perform(get("/items?page=1&size=5&group=STUDENT"))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(5)))
            .andExpect(jsonPath("$.hasNext", is(true)))
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

    private Item createItem(String itemName, int price, String imgName1, String imgName2,
        Member member) {
        ItemRequest itemRequest = new ItemRequest(itemName, price);
        itemRequest.getImageDTOList().add(new ImageDTO(imgName1));
        itemRequest.getImageDTOList().add(new ImageDTO(imgName2));
        return Item.createItem(itemRequest, member);
    }
}
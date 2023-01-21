package com.example.SSU_Rental.rating;

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
import java.util.ArrayList;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @BeforeEach
    public void clear(){
        ratingRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("리뷰 1개 등록")
    public void test1() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "mem-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "mem-img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        RatingRequest ratingRequest = new RatingRequest(10, "좋아요");
        String json = objectMapper.writeValueAsString(ratingRequest);

        //Act
        mockMvc.perform(post("/items/{itemId}/ratings",item.getId()).contentType(APPLICATION_JSON).content(json).cookie(cookie))
            .andExpect(status().isCreated())
            .andDo(print());


    }

    @Test
    @DisplayName("리뷰 1개 등록 실패 -> 점수는 1~10점 사이이고, 내용은 공백이 될 수 없습니다.")
    public void test2() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "mem-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "mem-img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        RatingRequest ratingRequest = new RatingRequest(11, "");
        String json = objectMapper.writeValueAsString(ratingRequest);

        //Act
        mockMvc.perform(post("/items/{itemId}/ratings",item.getId()).contentType(APPLICATION_JSON).content(json).cookie(cookie))
            //Assert
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is("400")))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.score").value("리뷰 점수는 0~10점 사이 입니다."))
            .andExpect(jsonPath("$.validation.content").value("리뷰는 공백이 될 수 없습니다."))
            .andDo(print());


    }

    @Test
    @DisplayName("리뷰 평균 점수 확인")
    public void test3() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "mem-img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "mem-img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        List<Rating> ratings = IntStream.rangeClosed(1, 20).mapToObj(i -> {
            return createRating(anotherMember, item, (i % 10) + 1, "좋아요" + i);
        }).collect(Collectors.toList());
        ratingRepository.saveAll(ratings);

        //Act
        mockMvc.perform(get("/items/{itemId}/ratings/scores",item.getId()))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(5.5)))
            .andDo(print());
    }

    @Test
    @DisplayName("리뷰 평균 점수 확인")
    public void test4() throws Exception {
        //Arrange
        Member member = createMember("user", "password", "유저", "STUDENT", "mem-img00");
        memberRepository.save(member);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);

        List<Member> members = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> createMember("user" + i, "password" + i, "유저" + i, "STUDENT", "img" + i))
            .collect(Collectors.toList());
        memberRepository.saveAll(members);

        List<Rating> ratings = new ArrayList<>();
        for (Member member1 : members) {
            ratings.add(createRating(member1,item,10,"좋아요"));
        }
        ratingRepository.saveAll(ratings);

        //Act
        mockMvc.perform(get("/items/{itemId}/ratings?page=1&size=5",item.getId()))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(5)))
            .andExpect(jsonPath("$.hasNext", is(true)))
            .andExpect(jsonPath("$.contents[0].score",is(10)))
            .andExpect(jsonPath("$.contents[0].nickname").value("유저21"))
            .andExpect(jsonPath("$.contents[0].content").value("좋아요"))
            .andExpect(jsonPath("$.contents[0].itemId").value(item.getId()))
            .andDo(print());
    }

    @Test
    @DisplayName("리뷰 수정")
    public void test5() throws Exception {
        //Arrange
        Member member = createMember("user", "password", "유저", "STUDENT", "mem-img00");
        memberRepository.save(member);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Member anotherMember = createMember("user2", "password", "유저2", "STUDENT", "mem-img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        Rating rating = createRating(anotherMember, item, 10, "좋아요");
        ratingRepository.save(rating);
        RatingEdit ratingEdit = new RatingEdit("상태가 Bad",5);
        String json = objectMapper.writeValueAsString(ratingEdit);

        //Act
        mockMvc.perform(patch("/items/{itemId}/ratings/{ratingId}",item.getId(),rating.getId()).contentType(APPLICATION_JSON).content(json).cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("리뷰 삭제")
    public void test6() throws Exception {
        //Arrange
        Member member = createMember("user", "password", "유저", "STUDENT", "mem-img00");
        memberRepository.save(member);
        Item item = createItem("아이템", 10000, "item-img01", "item-img02", member);
        itemRepository.save(item);
        Member anotherMember = createMember("user2", "password", "유저2", "STUDENT", "mem-img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        Rating rating = createRating(anotherMember, item, 10, "좋아요");
        ratingRepository.save(rating);

        //Act
        mockMvc.perform(delete("/items/{itemId}/ratings/{ratingId}",item.getId(),rating.getId()).cookie(cookie))
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

    private Rating createRating(Member loginMember,Item item,Integer score,String content){
        return Rating.createRating(loginMember,item,new RatingRequest(score,content));
    }
}
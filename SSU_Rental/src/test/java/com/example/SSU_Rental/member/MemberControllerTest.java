package com.example.SSU_Rental.member;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.SSU_Rental.board.Board;
import com.example.SSU_Rental.board.BoardRepository;
import com.example.SSU_Rental.board.BoardRequest;
import com.example.SSU_Rental.boardrp.Boardrp;
import com.example.SSU_Rental.boardrp.BoardrpRepository;
import com.example.SSU_Rental.boardrp.BoardrpRequest;
import com.example.SSU_Rental.image.ImageDTO;
import com.example.SSU_Rental.item.Item;
import com.example.SSU_Rental.item.ItemRepository;
import com.example.SSU_Rental.item.ItemRequest;
import com.example.SSU_Rental.login.LoginDTO;
import com.example.SSU_Rental.rating.Rating;
import com.example.SSU_Rental.rating.RatingRepository;
import com.example.SSU_Rental.rating.RatingRequest;
import com.example.SSU_Rental.rental.Rental;
import com.example.SSU_Rental.rental.RentalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardrpRepository boardrpRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @BeforeEach
    public void clear() {
        boardrpRepository.deleteAll();
        boardRepository.deleteAll();
        ratingRepository.deleteAll();
        rentalRepository.deleteAll();
        itemRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("회원 가입")
    public void test1() throws Exception {
        //Arrange
        MemberRequest memberRequest = MemberRequest.builder()
            .loginId("user1")
            .password("password1")
            .name("유저1")
            .group("STUDENT")
            .imageDTO(new ImageDTO("img01"))
            .build();
        String json = objectMapper.writeValueAsString(memberRequest);

        //Act
        mockMvc.perform(post("/members").contentType(APPLICATION_JSON).content(json))
            //Assert
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(print());
    }

    @Test
    @DisplayName("회원 가입시 로그인 아이디,비밀번호, 이름, 그룹중 하나라도 공백이 있으면 안된다.")
    public void test2() throws Exception {
        //Arrange
        MemberRequest memberRequest = MemberRequest.builder()
            .loginId("user1")
            .password("password1")
            .name("")
            .group("STUDENT")
            .imageDTO(new ImageDTO("img01"))
            .build();
        String json = objectMapper.writeValueAsString(memberRequest);

        //Act
        mockMvc.perform(post("/members").contentType(APPLICATION_JSON).content(json))
            //Assert
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code", is("400")))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andExpect(jsonPath("$.validation.name").value("회원 이름은 공백이 될 수 없습니다."))
            .andDo(print());
    }

    @Test
    @DisplayName("회원 1명 조회")
    public void test3() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);

        //Act
        mockMvc.perform(get("/members/{memberId}", member.getId()))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(member.getId()))
            .andExpect(jsonPath("$.loginId").value("user1"))
            .andExpect(jsonPath("$.name").value("유저1"))
            .andExpect(jsonPath("$.group").value("STUDENT"))
            .andExpect(jsonPath("$.imageDTO.imgName").value("img01"))
            .andDo(print());

    }

    @Test
    @DisplayName("회원 1명 수정")
    public void test4() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        MemberEdit memberEdit = new MemberEdit("user1-edit", new ImageDTO(""));
        String json = objectMapper.writeValueAsString(memberEdit);

        //Act
        mockMvc.perform(
                patch("/members/{memberId}", member.getId()).contentType(APPLICATION_JSON).content(json)
                    .cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andDo(print());

    }

    @Test
    @DisplayName("내가 작성한 게시글 목록 확인")
    public void test5() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        List<Board> boards = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> createBoard("제목" + i, "내용" + i, member)).collect(
                Collectors.toList());
        boardRepository.saveAll(boards);

        //Act
        mockMvc.perform(get("/members/boards?page=1&size=5").cookie(cookie))
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(5)))
            .andExpect(jsonPath("$.hasNext", is(true)))
            .andExpect(jsonPath("$.contents[0].title").value("제목21"))
            .andExpect(jsonPath("$.contents[0].content").value("내용21"))
            .andExpect(jsonPath("$.contents[0].nickname").value("유저1"))
            .andExpect(jsonPath("$.contents[0].views").value(0))
            .andExpect(jsonPath("$.contents[0].likes").value(0))
            .andExpect(jsonPath("$.contents[0].dislikes").value(0))
            .andExpect(jsonPath("$.contents[0].warns").value(0))
            .andDo(print());
    }

    @Test
    @DisplayName("내가 작성한 댓글 목록 확인")
    public void test6() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        Board board = createBoard("게시글-제목", "게시글-내용", member);
        boardRepository.save(board);
        List<Boardrp> boardrps = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> createBoardrp(board, member, "댓글" + i)).collect(Collectors.toList());
        boardrpRepository.saveAll(boardrps);

        //Act
        mockMvc.perform(get("/members/replys?page=1&size=5", board.getId()).cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(5)))
            .andExpect(jsonPath("$.hasNext", is(true)))
            .andExpect(jsonPath("$.contents[0].content").value("댓글21"))
            .andExpect(jsonPath("$.contents[0].nickname").value("유저1"))
            .andExpect(jsonPath("$.contents[0].boardId").value(board.getId()))
            .andDo(print());


    }

    @Test
    @DisplayName("내가 등록한 아이템 목록 확인")
    public void test7() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Cookie cookie = createCookie(member);
        List<Item> items = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> createItem("아이템" + i, i * 1000, "img" + i, "image" + i, member))
            .collect(Collectors.toList());
        itemRepository.saveAll(items);

        //Act
        mockMvc.perform(get("/members/items?page=1&size=5").cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(5)))
            .andExpect(jsonPath("$.hasNext", is(true)))
            .andExpect(jsonPath("$.contents[0].itemName").value("아이템21"))
            .andExpect(jsonPath("$.contents[0].group").value("STUDENT"))
            .andExpect(jsonPath("$.contents[0].status").value("AVAILABLE"))
            .andExpect(jsonPath("$.contents[0].memberName").value("유저1"))
            .andExpect(jsonPath("$.contents[0].price").value(21000))
            .andExpect(jsonPath("$.contents[0].imageDTOList[0].imgName").value("img21"))
            .andExpect(jsonPath("$.contents[0].imageDTOList[1].imgName").value("image21"))
            .andDo(print());

    }

    @Test
    @DisplayName("내가 한 리뷰 목록 보기")
    public void test8() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        List<Item> items = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> createItem("아이템" + i, i * 1000,"img"+i,"image"+i ,member)).collect(Collectors.toList());
        itemRepository.saveAll(items);
        int idx = 1;
        List<Rating> ratings = new ArrayList<>();
        for (Item item : items) {
            ratings.add(createRating(anotherMember,item,10,"좋아요"+idx));
            idx++;
        }
        ratingRepository.saveAll(ratings);

        //Act
        mockMvc.perform(get("/members/ratings?page=1&size=5").cookie(cookie))
            //Assert
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.page", is(1)))
            .andExpect(jsonPath("$.size", is(5)))
            .andExpect(jsonPath("$.totalPage", is(5)))
            .andExpect(jsonPath("$.hasNext", is(true)))
            .andExpect(jsonPath("$.contents[0].score",is(10)))
            .andExpect(jsonPath("$.contents[0].nickname").value("유저2"))
            .andExpect(jsonPath("$.contents[0].content").value("좋아요21"))
            .andExpect(jsonPath("$.contents[0].itemId").value(items.get(20).getId()))
            .andDo(print());


    }

    @Test
    @DisplayName("내가 한 렌탈 목록 보기")
    public void test9() throws Exception {
        //Arrange
        Member member = createMember("user1", "password1", "유저1", "STUDENT", "img01");
        memberRepository.save(member);
        Member anotherMember = createMember("user2", "password2", "유저2", "STUDENT", "img02");
        memberRepository.save(anotherMember);
        Cookie cookie = createCookie(anotherMember);
        List<Item> items = IntStream.rangeClosed(1, 21)
            .mapToObj(i -> createItem("아이템" + i, i * 1000,"img"+i,"image"+i ,member)).collect(Collectors.toList());
        itemRepository.saveAll(items);
        List<Rental> rentals = new ArrayList<>();
        for (Item item : items) {
            rentals.add(createRental(anotherMember,item));
        }
        rentalRepository.saveAll(rentals);

        //Act
        mockMvc.perform(get("/members/rentals?page=1&size=5").cookie(cookie))
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

    private Board createBoard(String title, String content, Member member) {
        return Board.createBoard(new BoardRequest(title, content), member);
    }

    private Boardrp createBoardrp(Board board, Member member, String content) {
        return Boardrp.createBoardrp(board, member, new BoardrpRequest(content));
    }

    private Item createItem(String itemName, int price, String imgName1, String imgName2,
        Member member) {
        ItemRequest itemRequest = new ItemRequest(itemName, price);
        itemRequest.getImageDTOList().add(new ImageDTO(imgName1));
        itemRequest.getImageDTOList().add(new ImageDTO(imgName2));
        return Item.createItem(itemRequest, member);
    }

    private Rating createRating(Member loginMember,Item item,Integer score,String content){
        return Rating.createRating(loginMember,item,new RatingRequest(score,content));
    }
    private Rental createRental(Member loginMember, Item item){
        return item.rental(loginMember);
    }
}
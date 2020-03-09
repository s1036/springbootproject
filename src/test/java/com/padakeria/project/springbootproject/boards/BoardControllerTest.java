package com.padakeria.project.springbootproject.boards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.padakeria.project.springbootproject.accounts.Account;
import com.padakeria.project.springbootproject.accounts.AccountRepository;
import com.padakeria.project.springbootproject.accounts.AccountRole;
import com.padakeria.project.springbootproject.boards.dto.BoardDto;
import com.padakeria.project.springbootproject.common.RestDocsConfiguration;
import com.padakeria.project.springbootproject.common.TestDescription;
import com.padakeria.project.springbootproject.common.WithMockCustomUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private WebApplicationContext context;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
//        MockitoAnnotations.initMocks(this);
    }

    @WithMockCustomUser(username = "이승현", email = "sj6239@naver.com", authorizedClientRegistrationId = "google")
    @Test
    @TestDescription("로그인 한 상태에서 게시글 작성 성공")
    public void createBoard_Success() throws Exception {


        String title = "테스트용 게시물";
        String content = "테스트용 내용";
        Set<AccountRole> accountRoles = new HashSet<>();
        accountRoles.add(AccountRole.USER);

        Account account = Account.builder()
                .name("이승현")
                .email("sj6239@naver.com")
                .profileImage("테스트용")
                .registrationId("google")
                .roles(accountRoles)
                .userPoint(0)
                .userSignUpDate(LocalDate.now())
                .build();
        Account saveAccount = accountRepository.save(account);

        Board board = Board.builder()
                .title(title)
                .content(content)
                .build();
        board.addAccount(saveAccount);


        BoardDto boardDto = modelMapper.map(board, BoardDto.class);


        mockMvc.perform(post("/api/board/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(boardDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(title))
                .andExpect(jsonPath("content").value(content))
                .andExpect(jsonPath("account").exists())
                .andExpect(jsonPath("createdDate").exists());
    }
}
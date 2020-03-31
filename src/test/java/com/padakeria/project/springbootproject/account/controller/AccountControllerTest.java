package com.padakeria.project.springbootproject.account.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.dto.SignUpForm;
import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.account.service.AccountService;
import com.padakeria.project.springbootproject.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @MockBean
    private JavaMailSender javaMailSender;


    @Test
    @TestDescription("회원가입 화면 확인")
    public void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @Test
    @TestDescription("회원가입 성공")
    public void signUp_success() throws Exception {

        String email = "test@gmail.com";
        String nickname = "test";
        String password = "12345678";

        mockMvc.perform(post("/sign-up")
                .param("nickname", nickname)
                .param("email", email)
                .param("password", password).with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(model().hasNoErrors())
                .andExpect(authenticated());

        assertTrue(accountRepository.existsByEmail(email));
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @TestDescription("이미 가입된 아이디로 가입 시도시 실패")
    public void signUp_with_invalid_input() throws Exception {
        Account account = Account.builder()
                .email("test@naver.com")
                .nickname("test")
                .password("123456")
                .build();

        Account savedAccount = accountRepository.save(account);

        mockMvc.perform(post("/sign-up")
                .param("nickname", savedAccount.getNickname())
                .param("email", savedAccount.getEmail())
                .param("password", "123456").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeHasFieldErrors("signUpForm", "nickname", "email"))
                .andExpect(unauthenticated());
    }

    @Test
    @TestDescription("잘못된 토큰으로 이메일 인증 시도시 실패")
    public void signUpEmailTokenCheck_wrong_token() throws Exception {
        Account account = signUpProcess();
        accountService.loginProcess(account);


        mockMvc.perform(get("/check-email-token")
                .param("token", "asdasdasdasd")
                .param("email", "test@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up-check"))
                .andExpect(model().attributeExists("error"));

    }

    @Test
    @TestDescription("이메일 인증 성공")
    public void signUpEmailTokenCheck_success() throws Exception {
        Account account = signUpProcess();
        accountService.loginProcess(account);

        mockMvc.perform(get("/check-email-token")
                .param("token", account.getEmailCheckToken())
                .param("email", account.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up-check"))
                .andExpect(model().attributeDoesNotExist("error"));

        Account vertifiedAccount = accountRepository.findByEmail(account.getEmail());

        assertTrue(vertifiedAccount.isEmailVerified());
    }

    @Test
    @TestDescription("이메일 재인증 성공")
    public void signUpEmailResend_success() throws Exception {

        Account account = signUpProcess();
        account.updateEmailCheckTokenGeneratedAt(LocalDateTime.now().minusHours(1));
        accountService.loginProcess(account);

        mockMvc.perform(get("/check-email-resend"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(model().hasNoErrors())
                .andExpect(authenticated());


        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    private Account signUpProcess() {
        String email = "test@gmail.com";

        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("test");
        signUpForm.setPassword("password");
        signUpForm.setEmail(email);

        return accountService.signUpProcess(signUpForm);
    }
}
package com.padakeria.project.springbootproject.common.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.account.dto.SignUpForm;
import com.padakeria.project.springbootproject.account.service.AccountService;
import com.padakeria.project.springbootproject.common.TestDescription;
import com.padakeria.project.springbootproject.common.WithAccount;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class MainControllerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MockMvc mockMvc;

    @After
    public void after() {
        accountRepository.deleteAll();
    }

    @Test
    @TestDescription("로그인 성공")
    public void login_success() throws Exception {

        signUpProcess();

        mockMvc.perform(post("/login")
                .param("username", "test")
                .param("password", "password")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated().withUsername("test"))
                .andExpect(redirectedUrl("/"));

        mockMvc.perform(post("/login")
                .param("username", "test@gmail.com")
                .param("password", "password")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated().withUsername("test"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @TestDescription("형식에 안맞는 값 입력시 로그인 실패")
    public void login_fail() throws Exception {
        signUpProcess();

        mockMvc.perform(post("/login")
                .param("username", "test123123123123123")
                .param("password", "password1231313123123")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    private Account signUpProcess() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("test");
        signUpForm.setPassword("password");
        signUpForm.setEmail("test@gmail.com");
        return accountService.signUpProcess(signUpForm);
    }

    @Test
    @TestDescription("로그아웃 성공")
    @WithAccount(username = "test")
    public void logout() throws Exception {
        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/"));
    }
}
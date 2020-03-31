package com.padakeria.project.springbootproject.common.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.dto.SignUpForm;
import com.padakeria.project.springbootproject.account.service.AccountService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private MockMvc mockMvc;

    @Test
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
    public void logout() throws Exception {
        Account account = signUpProcess();
        accountService.loginProcess(account);

        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/"));
    }
}
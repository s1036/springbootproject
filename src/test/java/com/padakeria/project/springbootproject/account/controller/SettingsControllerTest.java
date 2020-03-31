package com.padakeria.project.springbootproject.account.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.common.TestDescription;
import com.padakeria.project.springbootproject.common.WithAccount;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional

public class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @After
    public void after() {
        accountRepository.deleteAll();
    }

    @Test
    @WithAccount(username = "test")
    @TestDescription("프로필 설정 업데이트 성공")
    public void profileUpdate_success() throws Exception {
        String bio = "한 줄 소개 테스트";
        mockMvc.perform(post("/settings/profile").param("bio", bio)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("message"));


        Account account = accountRepository.findByNickname("test");


        assertEquals(bio, account.getBio());

    }

    @Test
    @WithAccount(username = "test")
    @TestDescription("형식에 맞지 않는 입력값으로 인해 프로필 업데이트 실패")
    public void profileUpdate_fail() throws Exception {
        String bio = "한 줄 소개 테스트 한 줄 소개 테스트 한 줄 소개 테스트 한 줄 소개 테스트 한 줄 소개 테스트 한 줄 소개 테스트 한 줄 소개 테스트 한 줄 소개 테스트 한 줄 소개 테스트 한 줄 소개 테스트 한 줄 소개 테스트 ";
        mockMvc.perform(post("/settings/profile").param("bio", bio)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(view().name("settings/profile"))
                .andExpect(model().hasErrors());


        Account account = accountRepository.findByNickname("test");


        assertNotEquals(bio, account.getBio());
    }

    @Test
    @WithAccount(username = "test")
    @TestDescription("비밀번호 변경 화면 띄우기")
    public void passwordUpdateForm() throws Exception {
        mockMvc.perform(get("/settings/password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(view().name("settings/password"));
    }

    @Test
    @WithAccount(username = "test")
    @TestDescription("비밀번호 변경 성공")
    public void passwordUpdate_success() throws Exception {

        String newPassword = "12345678";
        String confirmPassword = "12345678";

        mockMvc.perform(post("/settings/password")
                .param("newPassword", newPassword)
                .param("confirmPassword", confirmPassword)
                .with(csrf())).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/password"))
                .andExpect(flash().attributeExists("message"));


        Account account = accountRepository.findByNickname("test");
        assertTrue(passwordEncoder.matches(newPassword, account.getPassword()));
    }

    @Test
    @WithAccount(username = "test")
    @TestDescription("비밀번호 확인이 일치하지 않아 비밀번호 변경 실패")
    public void passwordUpdate_fail() throws Exception {

        String newPassword = "12345678";
        String confirmPassword = "asdadsasdsd";

        mockMvc.perform(post("/settings/password")
                .param("newPassword", newPassword)
                .param("confirmPassword", confirmPassword)
                .with(csrf())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("settings/password"));
    }

    @Test
    @WithAccount(username = "test")
    @TestDescription("알람 기능 설정 화면 띄우기")
    public void notificationsUpdateForm() throws Exception {
        mockMvc.perform(get("/settings/notifications"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("settings/notifications"))
                .andExpect(model().attributeExists("notificationsForm"));
    }

    @Test
    @WithAccount(username = "test")
    @TestDescription("알람 화면 설정 변경 성공")
    public void notificationsUpdate_success() throws Exception {

        mockMvc.perform(post("/settings/notifications")
                .param("studyCreatedByEmail", "true")
                .param("studyEnrollmentByEmail", "true")
                .param("studyUpdatedByEmail", "true")
                .param("studyCreatedByWeb", "false")
                .param("studyEnrollmentByWeb", "false")
                .param("studyUpdatedByWeb", "false")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/settings/notifications"));


        Account account = accountRepository.findByNickname("test");

        assertTrue(account.isStudyCreatedByEmail());
        assertTrue(account.isStudyEnrollmentByEmail());
        assertTrue(account.isStudyUpdatedByEmail());
        assertFalse(account.isStudyCreatedByWeb());
        assertFalse(account.isStudyEnrollmentByWeb());
        assertFalse(account.isStudyUpdatedByWeb());
    }

    @Test
    @WithAccount(username = "test")
    @TestDescription("닉네임 변경 화면 띄우기")
    public void nicknameUpdateForm() throws Exception {
        mockMvc.perform(get("/settings/account"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("settings/account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @Test
    @WithAccount(username = "test")
    @TestDescription("닉네임 변경 성공")
    public void nicknameUpdate_success() throws Exception {
        mockMvc.perform(post("/settings/account")
                .param("nickname", "chicken")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/settings/account"))
                .andExpect(authenticated().withUsername("chicken"));
    }
    @Test
    @WithAccount(username = "test")
    @TestDescription("이미 존재하는 닉네임을 입력해서 닉네임 변경 실패")
    public void nicknameUpdate_fail() throws Exception{
        mockMvc.perform(post("/settings/account")
                .param("nickname","test")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("settings/account"));
    }
}
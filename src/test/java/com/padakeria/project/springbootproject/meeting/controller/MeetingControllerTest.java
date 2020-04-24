package com.padakeria.project.springbootproject.meeting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.padakeria.project.springbootproject.common.TestDescription;
import com.padakeria.project.springbootproject.common.WithAccount;
import com.padakeria.project.springbootproject.common.entityfactory.AccountFactory;
import com.padakeria.project.springbootproject.common.entityfactory.PartyFactory;
import com.padakeria.project.springbootproject.meeting.dto.MeetingRequestDto;
import com.padakeria.project.springbootproject.party.domain.Member;
import com.padakeria.project.springbootproject.party.domain.Party;
import com.padakeria.project.springbootproject.party.service.PartyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountFactory accountFactory;
    @Autowired
    private PartyFactory partyFactory;
    @Autowired
    private PartyService partyService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @TestDescription("모임 생성 및 이미지 업로드 확인")
    @WithAccount(username = "test")
    public void createMeeting_with_image_success() throws Exception {
        Party party = partyFactory.createParty("test");
        Member member = partyService.enrollParty(party.getOwner(), party);
        partyService.acceptMember(member);
        MockMultipartFile mockMultipartFile1 = new MockMultipartFile("file", "chicken.png", "image/png", "asdsad".getBytes());
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("file", "pizza.png", "image/png", "1234".getBytes());
        MeetingRequestDto meetingRequestDto = MeetingRequestDto.builder()
                .name("야식 모임")
                .description("야식을 위한 모임")
                .location("서울 어딘가")
                .locationPointX(1.23213)
                .locationPointY(1.23123123)
                .maxPeople(5)
                .recruitStartingDate(LocalDateTime.now())
                .recruitEndingDate(LocalDateTime.now().plusDays(2))
                .meetingStartingDate(LocalDateTime.now().plusDays(3))
                .build();


        mockMvc.perform(multipart("/party/" + party.getId() + "/meeting/create")
                .file(mockMultipartFile1)
                .file(mockMultipartFile2)
                .param("name", "야식 모음!!")
                .param("ownerNickname", member.getAccount().getNickname())
                .param("description", "야식을 위한 모임")
                .param("location", "서울 어딘가")
                .param("locationPointX", meetingRequestDto.getLocationPointX().toString())
                .param("locationPointY", meetingRequestDto.getLocationPointY().toString())
                .param("maxPeople", "5")
                .param("recruitStartingDate", meetingRequestDto.getRecruitStartingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .param("recruitEndingDate", meetingRequestDto.getRecruitEndingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .param("meetingStartingDate", meetingRequestDto.getMeetingStartingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}
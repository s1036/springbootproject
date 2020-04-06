package com.padakeria.project.springbootproject.party.controller;

import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.common.TestDescription;
import com.padakeria.project.springbootproject.common.WithAccount;
import com.padakeria.project.springbootproject.party.domain.Party;
import com.padakeria.project.springbootproject.party.domain.PartyRepository;
import com.padakeria.project.springbootproject.party.dto.PartyRequestDto;
import com.padakeria.project.springbootproject.party.service.PartyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class PartyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PartyService partyService;


    @Test
    @TestDescription("모임 만들기 성공")
    @WithAccount(username = "test")
    public void createParty_success() throws Exception {
        String name = "테스트용 모임";
        mockMvc.perform(post("/party/create")
                .param("name", name)
                .param("location", "서울")
                .param("subject", "치킨")
                .param("description", "치킨 모임!")
                .param("profileImage", "")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/party/{[0-9]+}/profile"));

        List<Party> partyList = partyRepository.findAll();
        assertTrue(partyList.stream().anyMatch(party -> party.getOwner().getNickname().equals("test")));

        Party party = partyRepository.findAll().stream().findFirst().orElseThrow(RuntimeException::new);
        assertTrue(party.getMembers().stream().anyMatch(member -> member.getAccount().getNickname().equals("test")));
    }

    @Test
    @TestDescription("잘못된 값을 주입하여 모임 만들기 실패")
    @WithAccount(username = "test")
    public void createParty_fail_wrong_param() throws Exception {

        mockMvc.perform(post("/party/create")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }


    @Test
    @TestDescription("모임 리스트 조회하기")
    @WithAccount(username = "test")
    public void findPartyList_success() throws Exception {
        PartyRequestDto partyRequestDto = new PartyRequestDto();
        partyRequestDto.setOwner("test");
        for (int idx = 0; idx < 15; idx++)
            partyService.createParty(partyRequestDto);
        mockMvc.perform(get("/party/party-list")
                .param("page", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("partyList"));
    }

    @Test
    @TestDescription("없는 페이지를 조회하는 경우 실패")
    @WithAccount(username = "test")
    public void findPartyList_fail_invalid_page() throws Exception {
        PartyRequestDto partyRequestDto = new PartyRequestDto();
        partyRequestDto.setOwner("test");

        for (int idx = 0; idx < 30; idx++) {
            partyService.createParty(partyRequestDto);
        }

        mockMvc.perform(get("/party/party-list")
                .param("page", "1234"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/party/party-list"));
    }

    @Test
    @TestDescription("모임 세부정보 프로필 페이지 확인")
    @WithAccount(username = "test")
    public void viewPartyProfile_success() throws Exception {
        PartyRequestDto partyRequestDto = new PartyRequestDto();
        partyRequestDto.setOwner("test");
        Party party = partyService.createParty(partyRequestDto);

        mockMvc.perform(get("/party/" + party.getId() + "/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("party"))
                .andExpect(model().attribute("isOwner", true))
                .andExpect(model().attribute("isMember", true))
                .andExpect(view().name("party/profile"));
    }
    @Test
    @TestDescription("모임 멤버 확인")
    @WithAccount(username = "test")
    public void viewPartyMemberList_success() throws Exception{
        PartyRequestDto partyRequestDto = new PartyRequestDto();
        partyRequestDto.setOwner("test");
        Party party = partyService.createParty(partyRequestDto);

        mockMvc.perform(get("/party/" + party.getId() + "/member"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("memberList"))
                .andExpect(view().name("party/member"));
    }
}
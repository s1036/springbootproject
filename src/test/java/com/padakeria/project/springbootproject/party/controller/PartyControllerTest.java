package com.padakeria.project.springbootproject.party.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.common.TestDescription;
import com.padakeria.project.springbootproject.common.WithAccount;
import com.padakeria.project.springbootproject.common.entityfactory.AccountFactory;
import com.padakeria.project.springbootproject.common.entityfactory.PartyFactory;
import com.padakeria.project.springbootproject.party.domain.*;
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

import static org.junit.Assert.*;
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
    private PartyService partyService;
    @Autowired
    private AccountFactory accountFactory;
    @Autowired
    private PartyFactory partyFactory;

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
    public void findPartyListView_success() throws Exception {
        String username = "test";

        for (int idx = 0; idx < 15; idx++) {
            partyFactory.createParty(username);
        }

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
        String username = "test";

        for (int idx = 0; idx < 30; idx++) {
            partyFactory.createParty(username);
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
    public void viewPartyProfileView_success() throws Exception {
        String username = "test";
        Party party = partyFactory.createParty(username);

        mockMvc.perform(get("/party/" + party.getId() + "/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("party"))
                .andExpect(model().attribute("isManager", true))
                .andExpect(view().name("party/profile"));

    }

    @Test
    @TestDescription("모임 멤버 확인")
    @WithAccount(username = "test")
    public void viewPartyMemberListView_success() throws Exception {
        String username = "test";
        Party party = partyFactory.createParty(username);

        mockMvc.perform(get("/party/" + party.getId() + "/member"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("memberList"))
                .andExpect(view().name("party/member"));
    }

    @Test
    @TestDescription("모임 가입 신청 성공")
    @WithAccount(username = "account")
    public void enrollParty_success() throws Exception {
        String username = "test";
        Account account = accountFactory.createAccount(username);
        Party party = partyFactory.createParty(account.getNickname());


        mockMvc.perform(post("/party/" + party.getId() + "/enroll")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/party/" + party.getId() + "/profile"))
                .andExpect(flash().attributeExists("message"));

        boolean isMember = party.getMembers().stream().anyMatch(member -> member.getAccount().getNickname().equals("account"));
        assertTrue(isMember);
        Member enrolledMember = party.getMembers().stream().filter(member -> member.getAccount().getNickname().equals("account")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(MemberRole.TEMPORARY,enrolledMember.getRole());
    }

    @Test
    @TestDescription("이미 가입이 되있는 경우 모임 가입 신청 실패")
    @WithAccount(username = "account")
    public void enrollParty_fail_enrolled() throws Exception {
        String username = "test";
        Account account = accountFactory.createAccount(username);
        Party party = partyFactory.createParty(account.getNickname());

        Account tryAccount = accountFactory.createAccount("account");
        partyService.enrollParty(tryAccount, party);

        mockMvc.perform(post("/party/" + party.getId() + "/enroll")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/party/" + party.getId() + "/profile"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    @TestDescription("가입 신청 상태에서 가입 신청 취소하기 성공")
    @WithAccount(username = "account")
    public void cancelEnrollmentParty_success() throws Exception {
        String username = "test";
        Account account = accountFactory.createAccount(username);
        Party party = partyFactory.createParty(account.getNickname());

        Account tryAccount = accountFactory.createAccount("account");
        partyService.enrollParty(tryAccount, party);

        mockMvc.perform(post("/party/" + party.getId() + "/cancel")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/party/" + party.getId() + "/profile"))
                .andExpect(flash().attributeExists("message"));

        boolean isMember = party.getMembers().stream().noneMatch(member -> member.getAccount().getNickname().equals("account"));
        assertTrue(isMember);
    }

    @Test
    @TestDescription("가입 신청 상태에서 관리자가 가입 승인을 하여 일반 멤버로 등록")
    @WithAccount(username = "test")
    public void acceptEnrollmentParty_success() throws Exception {
        String username = "test";
        Party party = partyFactory.createParty(username);
        Account tryAccount = accountFactory.createAccount("account");
        Member member = partyService.enrollParty(tryAccount, party);

        mockMvc.perform(post("/party/accept/" + party.getId() + "/" + member.getId())
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/party/" + party.getId() + "/manage-member"));

        assertTrue(party.getAcceptedMember().stream().anyMatch(acceptedMember -> acceptedMember.getAccount().getNickname().equals("account")));
        assertTrue(party.getTemporaryMembers().stream().noneMatch(temporaryMember -> temporaryMember.getAccount().getNickname().equals("account")));

    }


    @Test
    @TestDescription("모임 탈퇴하기")
    @WithAccount(username = "account")
    public void secedeEnrollmentParty_success() throws Exception {
        String username = "test";
        Party party = partyFactory.createParty(username);
        Account tryAccount = accountFactory.createAccount("account");
        Member member = partyService.enrollParty(tryAccount, party);
        partyService.acceptMember(member);

        mockMvc.perform(post("/party/" + party.getId() + "/secede")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/party/" + party.getId() + "/profile"))
                .andExpect(flash().attributeExists("message"));


        assertTrue(party.getAcceptedMember().stream().noneMatch(acceptedMember -> acceptedMember.getAccount().getNickname().equals("account")));
    }

    @Test
    @TestDescription("모임 멤버 관리하기 뷰 띄우기")
    @WithAccount(username = "test")
    public void managePartyMemberView_success() throws Exception {

        Party party = partyFactory.createParty("test");
        Account temporaryMember = accountFactory.createAccount("temporaryMember");
        partyService.enrollParty(temporaryMember, party);

        mockMvc.perform(get("/party/" + party.getId() + "/manage-member"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("temporaryMemberList"))
                .andExpect(model().attributeExists("acceptedMemberList"))
                .andExpect(model().attribute("isManager", true))
                .andExpect(view().name("party/manage-member"));
    }

    @Test
    @TestDescription("모임 멤버 관리하기 추방기능 성공")
    @WithAccount(username = "test")
    public void banPartyMember_success() throws Exception {
        Party party = partyFactory.createParty("test");
        Account account = accountFactory.createAccount("banMember");
        Member member = partyService.enrollParty(account, party);
        partyService.acceptMember(member);

        mockMvc.perform(post("/party/ban/" + party.getId() + "/" + member.getId())
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/party/" + party.getId() + "/manage-member"));

        assertFalse(party.getMembers().contains(member));
    }

    @Test
    @TestDescription("모임 멤버 등급 변경")
    @WithAccount(username = "test")
    public void changeRolePartyMember_success() throws Exception {
        Party party = partyFactory.createParty("test");
        Account account = accountFactory.createAccount("changeRoleMember");
        Member member = partyService.enrollParty(account, party);
        partyService.acceptMember(member);

        mockMvc.perform(post("/party/change-role/" + party.getId() + "/" + member.getId())
                .param("role", "MANAGER")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/party/" + party.getId() + "/manage-member"));

        assertTrue(party.getMembers().stream()
                .filter(member1 -> member1.getAccount().getNickname().equals("changeRoleMember"))
                .anyMatch(changeMember -> changeMember.getRole() == MemberRole.MANAGER));
    }
}
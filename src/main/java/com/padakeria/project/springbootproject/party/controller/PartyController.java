package com.padakeria.project.springbootproject.party.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.CurrentUser;
import com.padakeria.project.springbootproject.party.domain.Member;
import com.padakeria.project.springbootproject.party.domain.Party;
import com.padakeria.project.springbootproject.party.dto.PartyRequestDto;
import com.padakeria.project.springbootproject.party.dto.PartyResponseDto;
import com.padakeria.project.springbootproject.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @GetMapping("/party/create")
    public String createPartyForm(@CurrentUser Account account, Model model) {
        model.addAttribute("partyRequestDto", new PartyRequestDto());
        model.addAttribute("account", account);
        return "party/create";
    }

    @PostMapping("/party/create")
    public String createParty(@CurrentUser Account account, @Valid PartyRequestDto partyRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return "party/create";
        }

        partyRequestDto.setOwner(account.getNickname());
        Party party = partyService.createParty(partyRequestDto);
        long id = party.getId();
        return "redirect:/party/" + id + "/profile";
    }

    @GetMapping("/party/party-list")
    public String viewPartyList(Integer page, Model model) {
        int pageValue;
        pageValue = page == null ? 1 : page;

        Page<PartyResponseDto> partyResponseDto = partyService.findPagedParty(pageValue);

        if (partyResponseDto.getTotalElements() == 0) {
            model.addAttribute("message", "모임이 없습니다.");
            return "party/party-list";
        }
        if (partyResponseDto.getTotalPages() < pageValue) {
            return "redirect:/party/party-list";
        }
        model.addAttribute("partyList", partyResponseDto);
        return "party/party-list";
    }

    @GetMapping("/party/{partyId}/profile")
    public String viewPartyProfile(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, Model model) {
        Member member = party.getCurrentMember(account);


        model.addAttribute("party", new PartyResponseDto(party));
        model.addAttribute("isMember", party.isMember(account));
        model.addAttribute("isTemporaryMember", member.isTemporaryMember());
        model.addAttribute("isAcceptMember", member.isAcceptedMember());
        model.addAttribute("isManager", member.isManager());
        return "party/profile";
    }

    @GetMapping("/party/{partyId}/member")
    public String viewPartyMemberList(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, Model model) {
        Member member = party.getCurrentMember(account);

        if (!member.isAcceptedMember()) {
            model.addAttribute("message", "권한이 없습니다");
            return "party/profile";
        }

        model.addAttribute("party", new PartyResponseDto(party));
        model.addAttribute("memberList", party.getAcceptedMember());
        model.addAttribute("isAcceptMember", member.isAcceptedMember());
        model.addAttribute("isManager", member.isManager());

        return "party/member";
    }

    @PostMapping("/party/{partyId}/enroll")
    public String enrollParty(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, RedirectAttributes redirectAttributes) {
        if (!party.isMember(account)) {
            partyService.enrollParty(account, party);
            redirectAttributes.addFlashAttribute("message", "가입 신청이 완료되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "가입 신청에 실패하였습니다. ");
        }
        return "redirect:/party/" + party.getId() + "/profile";
    }

    @PostMapping("/party/{partyId}/cancel")
    public String cancelEnrollmentParty(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, RedirectAttributes redirectAttributes) {
        Member member = party.getCurrentMember(account);
        if (member.isTemporaryMember()) {
            partyService.cancelEnrollParty(member, party);
            redirectAttributes.addFlashAttribute("message", "가입 취소가 완료되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "가입 취소가 실패하였습니다.");
        }
        return "redirect:/party/" + party.getId() + "/profile";
    }

    @PostMapping("/party/{partyId}/secede")
    public String secedeParty(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, RedirectAttributes redirectAttributes, Model model) {
        Member member = party.getCurrentMember(account);
        if (member.isOwner()) {
            redirectAttributes.addFlashAttribute("error", "모임의 소유자는 탈퇴 할 수 없습니다. 모임 관리를 이용해주세요");
            return "redirect:/party/" + party.getId() + "/profile";
        }
        if (member.isAcceptedMember()) {
            partyService.secedeParty(member, party);
            redirectAttributes.addFlashAttribute("message", "탈퇴가 완료되었습니다..");
        } else {
            redirectAttributes.addFlashAttribute("error", "탈퇴에 실패하였습니다.");
        }
        return "redirect:/party/" + party.getId() + "/profile";
    }

    @GetMapping("/party/{partyId}/manage-member")
    public String managePartyMember(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, RedirectAttributes redirectAttributes,Model model) {
        Member member = party.getCurrentMember(account);
        if (!member.isManager()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다");
            return "redirect:/party/" + party.getId() + "/profile";
        }

        model.addAttribute("party", new PartyResponseDto(party));
        model.addAttribute("acceptedMemberList", party.getAcceptedMember());
        model.addAttribute("temporaryMemberList", party.getTemporaryMembers());
        model.addAttribute("isAcceptMember", member.isAcceptedMember());
        model.addAttribute("isManager", member.isManager());
        model.addAttribute("isOwner", member.isOwner());


        return "party/manage-member";
    }

    @PostMapping("/party/accept/{partyId}/{memberId}")
    public String acceptPartyMember(@PathVariable(name = "partyId") Party party, @PathVariable(name = "memberId") Member enrollMember,
                                    @CurrentUser Account account, RedirectAttributes redirectAttributes) {
        Member currentMember = party.getCurrentMember(account);
        if (!currentMember.isManager()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다");
            return "redirect:/party/" + party.getId() + "/profile";
        }
        if (!enrollMember.isTemporaryMember()) {
            redirectAttributes.addFlashAttribute("message", "이미 허가된 회원입니다.");
            return "redirect:/party/" + party.getId() + "/manage-member";
        }

        partyService.acceptMember(enrollMember);

        redirectAttributes.addFlashAttribute("message", "가입을 승인하였습니다.");
        return "redirect:/party/" + party.getId() + "/manage-member";
    }
}

package com.padakeria.project.springbootproject.party.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.CurrentUser;
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

    @GetMapping("/party/{id}/profile")
    public String viewPartyProfile(@PathVariable Long id, @CurrentUser Account account, Model model) {
        String nickname = account.getNickname();
        Party party = partyService.findPartyById(id);

        model.addAttribute("party", new PartyResponseDto(party));
        model.addAttribute("isMember", party.isMember(nickname));
        model.addAttribute("isOwner", party.isOwner(nickname));
        return "party/profile";
    }

    @GetMapping("/party/{id}/member")
    public String viewPartyMemberList(@PathVariable Long id, @CurrentUser Account account, Model model) {
        String nickname = account.getNickname();
        Party party = partyService.findPartyById(id);
        if (!memberValidate(account, party)) {
            model.addAttribute("message", "권한이 없습니다");
            return "party/profile";
        }

        model.addAttribute("party", new PartyResponseDto(party));
        model.addAttribute("memberList", party.getMembers());
        model.addAttribute("isMember", party.isMember(nickname));
        model.addAttribute("isOwner", party.isOwner(nickname));

        return "party/member";
    }

    private boolean memberValidate(Account account, Party party) {
        return party.isMember(account.getNickname());
    }

}

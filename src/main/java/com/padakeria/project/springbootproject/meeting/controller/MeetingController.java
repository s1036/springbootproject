package com.padakeria.project.springbootproject.meeting.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.CurrentUser;
import com.padakeria.project.springbootproject.meeting.dto.MeetingRequestDto;
import com.padakeria.project.springbootproject.meeting.service.MeetingService;
import com.padakeria.project.springbootproject.party.domain.Member;
import com.padakeria.project.springbootproject.party.domain.Party;
import com.padakeria.project.springbootproject.party.dto.PartyResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping(value = "/party/{partyId}/meeting/create")
    public String createMeeting(@PathVariable(name = "partyId") Party party, Model model) {
        model.addAttribute("party", new PartyResponseDto(party));
        model.addAttribute("meetingRequestDto", new MeetingRequestDto());
        return "meeting/create";
    }

    @PostMapping(value = "/party/{partyId}/meeting/create")
    public String createMeeting(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, @Valid @ModelAttribute MeetingRequestDto meetingRequestDto
            , Errors errors,Model model) throws IOException {
        if (errors.hasErrors()) {
            model.addAttribute("party", new PartyResponseDto(party));
            return "meeting/create";
        }

        Member currentMember = party.getCurrentMember(account);

        meetingService.saveMeeting(meetingRequestDto, currentMember);

        return "redirect:/";
    }
}

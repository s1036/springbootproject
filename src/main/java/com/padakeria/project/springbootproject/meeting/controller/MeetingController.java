package com.padakeria.project.springbootproject.meeting.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.CurrentUser;
import com.padakeria.project.springbootproject.meeting.dto.MeetingRequestDto;
import com.padakeria.project.springbootproject.meeting.service.MeetingService;
import com.padakeria.project.springbootproject.party.domain.Member;
import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
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

    @PostMapping(value = "/party/{partyId}/meeting/create")
    public String createMeeting(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, @Valid @ModelAttribute MeetingRequestDto meetingRequestDto
            , Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return "redirect:/";
        }

        Member currentMember = party.getCurrentMember(account);

        meetingService.saveMeeting(meetingRequestDto, currentMember);

        return "redirect:/";
    }
}

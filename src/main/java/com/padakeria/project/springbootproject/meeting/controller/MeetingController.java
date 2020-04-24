package com.padakeria.project.springbootproject.meeting.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.CurrentUser;
import com.padakeria.project.springbootproject.meeting.dto.MeetingRequestDto;
import com.padakeria.project.springbootproject.meeting.dto.MeetingResponseDto;
import com.padakeria.project.springbootproject.meeting.service.MeetingService;
import com.padakeria.project.springbootproject.meeting.validator.MeetingRequestValidator;
import com.padakeria.project.springbootproject.party.domain.Member;
import com.padakeria.project.springbootproject.party.domain.Party;
import com.padakeria.project.springbootproject.party.dto.PartyResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MeetingController {

    private final MeetingService meetingService;
    private final MeetingRequestValidator meetingRequestValidator;

    @InitBinder(value = "meetingRequestDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(meetingRequestValidator);
    }

    @GetMapping(value = "/party/{partyId}/meeting/create")
    public String createMeeting(@PathVariable(name = "partyId") Party party, Model model) {

        model.addAttribute("party", new PartyResponseDto(party));
        model.addAttribute("meetingRequestDto", new MeetingRequestDto());
        return "meeting/create";
    }

    @PostMapping(value = "/party/{partyId}/meeting/create")
    public String createMeeting(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, @Valid @ModelAttribute MeetingRequestDto meetingRequestDto
            , Errors errors, Model model ) throws IOException {
        if (errors.hasErrors()) {
            model.addAttribute("party", new PartyResponseDto(party));
            model.addAttribute("message","입력값을 확인해주세요");
            return "meeting/create";
        }

        Member currentMember = party.getCurrentMember(account);

        meetingService.saveMeeting(meetingRequestDto, currentMember);
        return "redirect:/";
    }

    @GetMapping("/party/{partyId}/meeting/join")
    public String viewPartyList(@PathVariable(name = "partyId") Party party, @CurrentUser Account account, Integer page, Model model) {
        int pageValue;
        pageValue = page == null ? 1 : page;

        Page<MeetingResponseDto> meetingResponseDto = meetingService.findPagedMeeting(pageValue,party);

        Member member = party.getCurrentMember(account);
        model.addAttribute("isMember", party.isMember(account));
        model.addAttribute("isAcceptMember", member.isAcceptedMember());
        model.addAttribute("isManager", member.isManager());
        model.addAttribute("party", new PartyResponseDto(party));
        if (meetingResponseDto.getTotalElements() == 0) {
            model.addAttribute("message", "모임이 없습니다.");
            return "meeting/join";
        }
        if (meetingResponseDto.getTotalPages() < pageValue) {
            return "redirect:/meeting/join";
        }
        model.addAttribute("meetingList", meetingResponseDto);
        return "meeting/join";
    }

}

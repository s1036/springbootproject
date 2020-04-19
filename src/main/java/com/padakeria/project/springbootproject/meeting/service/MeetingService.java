package com.padakeria.project.springbootproject.meeting.service;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.meeting.domain.Meeting;
import com.padakeria.project.springbootproject.meeting.domain.MeetingRepository;
import com.padakeria.project.springbootproject.meeting.dto.MeetingRequestDto;
import com.padakeria.project.springbootproject.party.domain.Member;
import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public Meeting saveMeeting(MeetingRequestDto meetingRequestDto, Member member) throws IOException {
        Account account = member.getAccount();
        Party party = member.getParty();
        Meeting meeting;

        if (meetingRequestDto.getFile() != null && !meetingRequestDto.getFile().isEmpty()) {
            List<String> imagesUrl = saveImages(meetingRequestDto.getFile());
            meeting = meetingRepository.save(meetingRequestDto.toEntityWithImages(account, party, imagesUrl));
        } else {
            meeting = meetingRepository.save(meetingRequestDto.toEntityWithoutImages(account, party));
        }

        meeting.addAccount(account);
        return meeting;
    }

    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imagesUrl = new ArrayList<>();
        for (MultipartFile image : images) {
            String filename = image.getOriginalFilename();
            String savePath = "/images/";
            String filePath = savePath + UUID.randomUUID().toString().replace("-", "") + filename;
            imagesUrl.add(filePath);
            File file = new File(filePath);
            image.transferTo(file);
        }
        return imagesUrl;
    }
}

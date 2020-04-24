package com.padakeria.project.springbootproject.meeting.service;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.meeting.domain.Meeting;
import com.padakeria.project.springbootproject.meeting.domain.MeetingRepository;
import com.padakeria.project.springbootproject.meeting.dto.MeetingRequestDto;
import com.padakeria.project.springbootproject.meeting.dto.MeetingResponseDto;
import com.padakeria.project.springbootproject.party.domain.Member;
import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public Meeting saveMeeting(MeetingRequestDto meetingRequestDto, Member member) throws IOException {
        Account account = member.getAccount();
        Party party = member.getParty();
        Meeting meeting;
        List<MultipartFile> file = meetingRequestDto.getFile();

        if (fileCheck(file)) {
            List<String> imagesUrl = saveImages(file);
            meeting = meetingRepository.save(meetingRequestDto.toEntityWithImages(account, party, imagesUrl));
        } else {
            meeting = meetingRepository.save(meetingRequestDto.toEntityWithoutImages(account, party));
        }

        meeting.addAccount(account);
        return meeting;
    }

    public Page<MeetingResponseDto> findPagedMeeting(Integer page) {
        Pageable realPage = PageRequest.of(page <= 0 ? 0 : page - 1, 5, new Sort(Sort.Direction.DESC, "creation"));
        Page<Meeting> meetings = meetingRepository.findAll(realPage);

        long totalElements = meetings.getTotalElements();
        return new PageImpl<>(meetings.stream().map(MeetingResponseDto::new).collect(Collectors.toList()), realPage, totalElements);
    }
    private boolean fileCheck(List<MultipartFile> file) {
        return file != null && !Objects.equals(file.get(0).getOriginalFilename(), "") && !file.isEmpty();
    }

    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imagesUrl = new ArrayList<>();
        for (MultipartFile image : images) {
            String filename = image.getOriginalFilename();
            String savePath = "images/";
            File pathDir = new File(savePath);
            if (!pathDir.exists()) {
                pathDir.mkdir();
            }
            String filePath = savePath + UUID.randomUUID().toString().replace("-", "") + filename;
            imagesUrl.add(filePath);
            File file = new File(filePath);
            image.transferTo(file);
        }
        return imagesUrl;
    }
}

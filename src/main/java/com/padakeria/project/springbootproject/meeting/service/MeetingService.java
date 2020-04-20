package com.padakeria.project.springbootproject.meeting.service;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.meeting.domain.Meeting;
import com.padakeria.project.springbootproject.meeting.domain.MeetingRepository;
import com.padakeria.project.springbootproject.meeting.dto.MeetingRequestDto;
import com.padakeria.project.springbootproject.party.domain.Member;
import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    private boolean fileCheck(List<MultipartFile> file) {
        return file != null && !Objects.equals(file.get(0).getOriginalFilename(), "") && !file.isEmpty();
    }

    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imagesUrl = new ArrayList<>();
        for (MultipartFile image : images) {
            String filename = image.getOriginalFilename();
            String savePath = "/images/";
            File pathDir = new File(savePath);
            if (!pathDir.exists()) {
                pathDir.mkdir();
            }
            String filePath = savePath + UUID.randomUUID().toString().replace("-", "") + filename;
            imagesUrl.add(filePath);
            File file = new File(filePath);

            log.info(file.getAbsolutePath() + "절대 경로!!");
            log.info(file.getPath()+"경로");

            image.transferTo(file);
        }
        return imagesUrl;
    }
}

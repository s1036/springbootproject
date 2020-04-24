package com.padakeria.project.springbootproject.meeting.dto;

import com.padakeria.project.springbootproject.meeting.domain.Meeting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MeetingResponseDto {
    private Long id;
    private String owner;
    private Long partyId;
    private int numberOfPeople;
    private String name;
    private String description;
    private String thumbnailUrl;
    private String location;
    private Point locationPoint;
    private Integer maxPeople;
    private LocalDateTime recruitStaringDate;
    private LocalDateTime recruitEndingDate;
    private LocalDateTime meetingStartingDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime creation;

    public MeetingResponseDto(Meeting meeting) {
        this.id = meeting.getId();
        this.owner = meeting.getOwner().getNickname();
        this.partyId = meeting.getParty().getId();
        this.numberOfPeople = meeting.getAccounts().size();
        this.name = meeting.getName();
        this.description = meeting.getDescription();
        this.location = meeting.getLocation();
        this.locationPoint = meeting.getLocationPoint();
        this.maxPeople = meeting.getMaxPeople();
        this.recruitStaringDate = meeting.getRecruitStaringDate();
        this.recruitEndingDate = meeting.getRecruitEndingDate();
        this.meetingStartingDate = meeting.getMeetingStartingDate();
        this.creation = meeting.getCreation();
        this.thumbnailUrl = meeting.getImagesUrl().stream().findFirst().orElseGet(String::new);
    }
}

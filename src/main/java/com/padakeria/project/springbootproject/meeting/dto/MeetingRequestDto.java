package com.padakeria.project.springbootproject.meeting.dto;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.meeting.domain.Meeting;
import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.geo.Point;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MeetingRequestDto {

    @Length(min = 5, max = 20)
    private String name;

    private String description;

    @NotEmpty
    private String location;

    private Double locationPointX;

    private Double locationPointY;

    @Positive
    @Min(value = 2)
    @Max(value = 200)
    private Integer maxPeople;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recruitStartingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recruitEndingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetingStartingDate;

    private List<MultipartFile> file;


    public Meeting toEntityWithImages(Account owner, Party party, List<String> imagesUrl) {
        Point locationPoint = new Point(locationPointX, locationPointY);
        return Meeting.builder()
                .owner(owner)
                .party(party)
                .name(this.name)
                .description(this.description)
                .location(this.location)
                .locationPoint(locationPoint)
                .maxPeople(this.maxPeople)
                .recruitStaringDate(this.recruitStartingDate)
                .recruitEndingDate(this.recruitEndingDate)
                .meetingStartingDate(this.meetingStartingDate)
                .imagesUrl(imagesUrl)
                .build();
    }

    public Meeting toEntityWithoutImages(Account owner, Party party) {
        Point locationPoint = new Point(locationPointX, locationPointY);
        return Meeting.builder()
                .owner(owner)
                .party(party)
                .name(this.name)
                .description(this.description)
                .location(this.location)
                .locationPoint(locationPoint)
                .maxPeople(this.maxPeople)
                .recruitStaringDate(this.recruitStartingDate)
                .recruitEndingDate(this.recruitEndingDate)
                .meetingStartingDate(this.meetingStartingDate)
                .build();
    }

    @Builder
    public MeetingRequestDto(String name, String description, String location, Double locationPointX, Double locationPointY, Integer maxPeople, LocalDateTime recruitStartingDate, LocalDateTime recruitEndingDate, LocalDateTime meetingStartingDate) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.locationPointX = locationPointX;
        this.locationPointY = locationPointY;
        this.maxPeople = maxPeople;
        this.recruitStartingDate = recruitStartingDate;
        this.recruitEndingDate = recruitEndingDate;
        this.meetingStartingDate = meetingStartingDate;
    }
}

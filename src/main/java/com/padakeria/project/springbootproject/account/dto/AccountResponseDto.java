package com.padakeria.project.springbootproject.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponseDto {
    private String email;
    private String nickname;
    private boolean emailVerified;
    private LocalDateTime joinedAt;
    private String bio;
    private String url;
    private String occupation;
    private String location;
    private String profileImage;
    private boolean studyCreatedByEmail;
    private boolean studyCreatedByWeb;
    private boolean studyEnrollmentByEmail;
    private boolean studyEnrollmentByWeb;
    private boolean studyUpdatedByEmail;
    private boolean studyUpdatedByWeb;

    @Builder
    public AccountResponseDto(String email, String nickname, boolean emailVerified, LocalDateTime joinedAt, String bio, String url, String occupation, String location, String profileImage, boolean studyCreatedByEmail, boolean studyCreatedByWeb, boolean studyEnrollmentByEmail, boolean studyEnrollmentByWeb, boolean studyUpdatedByEmail, boolean studyUpdatedByWeb) {
        this.email = email;
        this.nickname = nickname;
        this.emailVerified = emailVerified;
        this.joinedAt = joinedAt;
        this.bio = bio;
        this.url = url;
        this.occupation = occupation;
        this.location = location;
        this.profileImage = profileImage;
        this.studyCreatedByEmail = studyCreatedByEmail;
        this.studyCreatedByWeb = studyCreatedByWeb;
        this.studyEnrollmentByEmail = studyEnrollmentByEmail;
        this.studyEnrollmentByWeb = studyEnrollmentByWeb;
        this.studyUpdatedByEmail = studyUpdatedByEmail;
        this.studyUpdatedByWeb = studyUpdatedByWeb;
    }
}

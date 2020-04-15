package com.padakeria.project.springbootproject.account.domain;

import com.padakeria.project.springbootproject.account.dto.NotificationsForm;
import com.padakeria.project.springbootproject.account.dto.Profile;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified = false;

    private String emailCheckToken;
    private LocalDateTime emailCheckTokenGeneratedAt;
    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob
    private String profileImage;

    private boolean partyCreatedByEmail = false;

    private boolean partyCreatedByWeb = true;

    private boolean partyEnrollmentByEmail = false;

    private boolean partyEnrollmentByWeb = true;

    private boolean partyUpdatedByEmail = false;

    private boolean partyUpdatedByWeb = true;


    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        boolean isValid = this.emailCheckToken.equals(token);
        if (isValid) {
            completeSignUp();
            return true;
        } else return false;
    }

    private void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
    }

    public Account updateProfile(Profile profile) {

        this.bio = profile.getBio();
        this.occupation = profile.getOccupation();
        this.profileImage = profile.getProfileImage();
        this.location = profile.getLocation();
        this.url = profile.getUrl();
        return this;
    }

    public Account updatePassword(String password) {
        this.password = password;
        return this;
    }

    public Account updateNotifications(NotificationsForm notificationsForm) {
        this.partyCreatedByEmail = notificationsForm.isPartyCreatedByEmail();
        this.partyCreatedByWeb = notificationsForm.isPartyCreatedByWeb();
        this.partyEnrollmentByEmail = notificationsForm.isPartyEnrollmentByEmail();
        this.partyEnrollmentByWeb = notificationsForm.isPartyEnrollmentByWeb();
        this.partyUpdatedByEmail = notificationsForm.isPartyUpdatedByEmail();
        this.partyUpdatedByWeb = notificationsForm.isPartyUpdatedByWeb();

        return this;
    }

    public Account updateNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }


    public void updateEmailCheckTokenGeneratedAt(LocalDateTime minusHours) {
        this.emailCheckTokenGeneratedAt = minusHours;
    }
}

package com.padakeria.project.springbootproject.account.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationsForm {


    private boolean partyCreatedByEmail;

    private boolean partyCreatedByWeb;

    private boolean partyEnrollmentByEmail;

    private boolean partyEnrollmentByWeb;

    private boolean partyUpdatedByEmail;

    private boolean partyUpdatedByWeb;


}

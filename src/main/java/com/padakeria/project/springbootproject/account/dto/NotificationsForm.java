package com.padakeria.project.springbootproject.account.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationsForm {


    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentByEmail;

    private boolean studyEnrollmentByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;


}

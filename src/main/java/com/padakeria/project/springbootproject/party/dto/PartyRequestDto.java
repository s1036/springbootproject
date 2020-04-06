package com.padakeria.project.springbootproject.party.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PartyRequestDto {
    @NotEmpty
    @Length(min =  3,max = 30)
    private String name;
    private String owner;

    @NotEmpty
    private String location;
    @NotEmpty
    private String subject;
    private String description;
    private String profileImage;
}


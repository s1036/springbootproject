package com.padakeria.project.springbootproject.party.dto;

import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartyResponseDto {
    private Long id;
    private String name;
    private String owner;
    private String location;
    private String subject;
    private String description;
    private String profileImage;


    public PartyResponseDto(Party party) {
        this.id = party.getId();
        this.name = party.getName();
        this.owner = party.getOwner().getNickname();
        this.location = party.getLocation();
        this.subject = party.getSubject();
        this.description = party.getDescription();
        this.profileImage = party.getProfileImage();
    }
}

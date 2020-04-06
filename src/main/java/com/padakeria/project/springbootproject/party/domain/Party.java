package com.padakeria.project.springbootproject.party.domain;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.member.domain.Member;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    private Account owner;

    private String location;

    private LocalDateTime creation;

    private String subject;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "party",cascade = CascadeType.REMOVE)
    private Set<Member> members = new LinkedHashSet<>();

    @Lob
    private String description;

    @Lob
    private String profileImage;

    @Builder
    public Party(Account owner, String name, String location, LocalDateTime creation, String subject, String description, String profileImage) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.creation = creation;
        this.subject = subject;
        this.description = description;
        this.profileImage = profileImage;
    }



    public boolean isOwner(String nickname) {
        return this.owner.getNickname().equals(nickname);
    }

    public boolean isMember(String nickname) {
        return members.stream().anyMatch(member -> member.getAccount().getNickname().equals(nickname));
    }


// TODO: 2020-04-05 게시판, 공지,
}

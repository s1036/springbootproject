package com.padakeria.project.springbootproject.member.domain;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private int point;
    private String role;
    private LocalDateTime signupDate;

    @Builder
    public Member(Party party, Account account, int point, String role, LocalDateTime signupDate) {
        this.party = party;
        this.account = account;
        this.point = point;
        this.role = role;
        this.signupDate = signupDate;
        this.addParty(party);
    }


    public void addParty(Party party) {
        this.party = party;
        party.getMembers().add(this);
    }
}

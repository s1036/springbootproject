package com.padakeria.project.springbootproject.party.domain;

import com.padakeria.project.springbootproject.account.domain.Account;
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
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;
    private LocalDateTime signupDate;

    @Builder
    public Member(Party party, Account account, int point, MemberRole role, LocalDateTime signupDate) {
        this.party = party;
        this.account = account;
        this.point = point;
        this.role = role;
        this.signupDate = signupDate;
    }

    public void addParty(Party party) {
        this.party = party;
        this.party.getMembers().add(this);
    }

    public void deleteParty(Party party) {
        party.getMembers().remove(this);
        this.party = null;
    }

    public void changeRole(MemberRole role) {
        this.role = role;
    }

    public boolean isTemporaryMember() {
        return (this.role == MemberRole.TEMPORARY);
    }

    public boolean isAcceptedMember() {
        return (this.role == MemberRole.USER) || (this.role == MemberRole.MANAGER) || (this.role == MemberRole.OWNER);
    }

    public boolean isManager() {
        return (this.role == MemberRole.MANAGER) || (this.role == MemberRole.OWNER);
    }

    public boolean isOwner() {
        return (this.role == MemberRole.OWNER);
    }
}

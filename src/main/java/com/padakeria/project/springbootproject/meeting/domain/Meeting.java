package com.padakeria.project.springbootproject.meeting.domain;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Account owner;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Party party;

    @OneToMany
    @JoinColumn(name = "account_id")
    private Set<Account> accounts;

    private String name;

    @Lob
    private String description;
    @ElementCollection
    private Collection<String> imagesUrl;

    private String location;

    private Point locationPoint;

    private Integer maxPeople;
    private LocalDateTime recruitStaringDate;
    private LocalDateTime recruitEndingDate;
    private LocalDateTime meetingStartingDate;

    private LocalDateTime creation;

    @Builder
    public Meeting(Account owner, Party party, Set<Account> accounts, String name, String description, List<String> imagesUrl, String location, Point locationPoint, Integer maxPeople, LocalDateTime recruitStaringDate, LocalDateTime recruitEndingDate, LocalDateTime meetingStartingDate) {
        this.owner = owner;
        this.party = party;
        this.accounts = accounts;
        this.name = name;
        this.description = description;
        this.imagesUrl = imagesUrl;
        this.location = location;
        this.locationPoint = locationPoint;
        this.maxPeople = maxPeople;
        this.recruitStaringDate = recruitStaringDate;
        this.recruitEndingDate = recruitEndingDate;
        this.meetingStartingDate = meetingStartingDate;
        this.creation = LocalDateTime.now();
        this.accounts = new LinkedHashSet<>();
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public boolean isJoinable(Account account) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(this.recruitStaringDate) && now.isBefore(this.recruitEndingDate) && !isMember(account);
    }

    private boolean isMember(Account enrollmentAccount) {
        return this.accounts.stream().anyMatch(account -> account.equals(enrollmentAccount));
    }
}

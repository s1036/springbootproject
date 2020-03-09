package com.padakeria.project.springbootproject.accounts;

import com.padakeria.project.springbootproject.boards.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "REGISTRATION_ID", nullable = false)
    private String registrationId;

    @Column(name = "SIGN_UP_DATE")
    private LocalDate userSignUpDate;

    @Column(name = "USER_POINT")
    private int userPoint;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

    @OneToMany(mappedBy = "account")
    private List<Board>  boardList = new ArrayList<>();

    @Builder
    public Account(String profileImage, String email, String name, String registrationId, LocalDate userSignUpDate, int userPoint, Set<AccountRole> roles) {
        this.profileImage = profileImage;
        this.email = email;
        this.name = name;
        this.registrationId = registrationId;
        this.userSignUpDate = userSignUpDate;
        this.userPoint = userPoint;
        this.roles = roles;
    }

    public Account update(String name, String picture) {
        this.name = name;
        this.profileImage = picture;
        return this;
    }
}

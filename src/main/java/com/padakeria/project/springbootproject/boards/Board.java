package com.padakeria.project.springbootproject.boards;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.padakeria.project.springbootproject.accounts.Account;
import com.padakeria.project.springbootproject.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "BOARD_ID")
    private Long id;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private Account account;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "TITLE", nullable = false)
    private String title;


    @Builder
    public Board(String content, String title) {
        this.content = content;
        this.title = title;
    }

    void addAccount(Account account) {
        this.account = account;
        this.account.getBoardList().add(this);
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
package com.padakeria.project.springbootproject.boards.dto;

import com.padakeria.project.springbootproject.accounts.Account;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class BoardDto implements Serializable {
    @NotNull
    private Account account;
    @NotNull
    private String content;
    @NotNull
    private String title;
    @NotNull
    private LocalDateTime createdDate;
}

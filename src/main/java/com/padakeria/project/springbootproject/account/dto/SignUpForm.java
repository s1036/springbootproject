package com.padakeria.project.springbootproject.account.dto;


import com.padakeria.project.springbootproject.account.validator.group.LengthCheckGroup;
import com.padakeria.project.springbootproject.account.validator.group.NotBlankGroup;
import com.padakeria.project.springbootproject.account.validator.group.PatternGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class SignUpForm {
    @NotBlank(groups = NotBlankGroup.class)
    @Length(min = 3, max = 20, groups = LengthCheckGroup.class)
    @Pattern(regexp = "^[0-9ㄱ-ㅎ가-힣a-zA-Z_-]{3,20}$", groups = PatternGroup.class)
    private String nickname;

    @NotBlank(message = "이메일을 입력해주세요", groups = NotBlankGroup.class)
    @Email
    private String email;

    @NotBlank(groups = NotBlankGroup.class)
    @Length(min = 6, max = 20, groups = LengthCheckGroup.class)
    private String password;
}

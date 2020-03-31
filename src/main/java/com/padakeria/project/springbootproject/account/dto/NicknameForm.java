package com.padakeria.project.springbootproject.account.dto;

import com.padakeria.project.springbootproject.account.validator.group.LengthCheckGroup;
import com.padakeria.project.springbootproject.account.validator.group.NotBlankGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class NicknameForm {
    @NotBlank(groups = NotBlankGroup.class)
    @Length(min = 3, max = 20, groups = LengthCheckGroup.class)
    @Pattern(regexp = "^[0-9ㄱ-ㅎ가-힣a-zA-Z_-]{3,20}$")
    private String nickname;

}

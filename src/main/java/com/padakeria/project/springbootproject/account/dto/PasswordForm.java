package com.padakeria.project.springbootproject.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class PasswordForm {

    @Length(min = 8, max = 50)
    private String confirmPassword;

    @Length(min = 8, max = 50)
    private String newPassword;

}

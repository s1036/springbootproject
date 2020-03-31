package com.padakeria.project.springbootproject.account.dto;

import com.padakeria.project.springbootproject.account.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class Profile {

    @Length(max = 35)
    private String bio;

    @Length(max = 50)
    private String url;

    @Length(max = 35)
    private String occupation;

    @Length(max = 35)
    private String location;

    private String profileImage;

}

package com.padakeria.project.springbootproject.config.auth.dto;

import com.padakeria.project.springbootproject.accounts.Account;
import com.padakeria.project.springbootproject.accounts.AccountRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class OauthAttributes {
    private String registraionId;
    private String userNameAttributeName;
    private Map<String, Object> attributes;
    private String name;
    private String email;
    private String picture;


    @Builder
    public OauthAttributes(String registraionId, String userNameAttributeName, Map<String, Object> attributes, String name, String email, String picture) {
        this.registraionId = registraionId;
        this.userNameAttributeName = userNameAttributeName;
        this.attributes = attributes;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OauthAttributes of(String registraionId, String userNameAttributeName, Map<String, Object> attributes) {
        // TODO: 2020-03-09 분기
//        if (registraionId.equals("google")) {
            return ofGoogle(registraionId, userNameAttributeName, attributes);
//        }
    }

    private static OauthAttributes ofGoogle(String registraionId, String userNameAttributeName, Map<String, Object> attributes) {
        return OauthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .registraionId(registraionId)
                .attributes(attributes)
                .userNameAttributeName(userNameAttributeName)
                .build();
    }

    public Account toEntity() {
        Set<AccountRole> accountRoles = new HashSet<>();
        accountRoles.add(AccountRole.USER);

    return Account.builder()
            .email(email)
            .name(name)
            .profileImage(picture)
            .registrationId(registraionId)
            .roles(accountRoles)
            .userPoint(0)
            .userSignUpDate(LocalDate.now())
            .build();
    }
}

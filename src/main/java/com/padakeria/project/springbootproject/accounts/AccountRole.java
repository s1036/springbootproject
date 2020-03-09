package com.padakeria.project.springbootproject.accounts;

import lombok.Getter;

@Getter
public enum AccountRole {
    USER("ROLE_USER", "일반 사용자"),
    GUEST("ROLE_GUEST", "손님");
    private final String key;
    private final String title;

    AccountRole(String key, String title) {
        this.key = key;
        this.title = title;
    }
}

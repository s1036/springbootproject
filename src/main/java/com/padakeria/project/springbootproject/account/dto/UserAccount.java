package com.padakeria.project.springbootproject.account.dto;

import com.padakeria.project.springbootproject.account.domain.Account;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;

@Getter
public class UserAccount extends User {

    private Account account;

    public UserAccount(Account account, Set<SimpleGrantedAuthority> authorities) {
        super(account.getNickname(), account.getPassword(), authorities);
        this.account = account;
    }
}

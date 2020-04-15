package com.padakeria.project.springbootproject.common.entityfactory;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountFactory {
    private final AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account account;
        if (accountRepository.findByNickname(nickname) == null) {
            account = Account.builder()
                    .nickname(nickname)
                    .email(nickname + "@email.com")
                    .partyCreatedByWeb(true)
                    .partyEnrollmentByWeb(true)
                    .partyUpdatedByWeb(true)
                    .build();
        } else {
            account = accountRepository.findByNickname(nickname);
        }
        return accountRepository.save(account);
    }
}

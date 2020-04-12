package com.padakeria.project.springbootproject.common.entityfactory;


import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.party.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PartyFactory {
    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final AccountFactory accountFactory;
    private final AccountRepository accountRepository;

    public Party createParty(String nickname) {
        Account account;
        if (accountRepository.findByNickname(nickname) == null) {
            account = accountFactory.createAccount(nickname);
        } else {
            account = accountRepository.findByNickname(nickname);
        }
        Party party = Party.builder()
                .owner(account)
                .build();

        Member member = Member.builder()
                .account(account)
                .party(party)
                .point(0)
                .role(MemberRole.OWNER)
                .signupDate(LocalDateTime.now())
                .build();

        member.addParty(party);
        memberRepository.save(member);
        return partyRepository.save(party);
    }
}

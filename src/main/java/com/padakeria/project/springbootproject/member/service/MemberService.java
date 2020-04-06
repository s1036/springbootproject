package com.padakeria.project.springbootproject.member.service;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.member.domain.Member;
import com.padakeria.project.springbootproject.member.domain.MemberRepository;
import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void signupParty(Account account, Party party) {
        Member member = Member.builder()
                .account(account)
                .party(party)
                .point(0)
                .role("관리자")
                .signupDate(LocalDateTime.now())
                .build();
        memberRepository.save(member);
    }
}

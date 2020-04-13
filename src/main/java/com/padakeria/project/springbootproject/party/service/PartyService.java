package com.padakeria.project.springbootproject.party.service;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.common.exceptions.MemberChangeException;
import com.padakeria.project.springbootproject.party.domain.*;
import com.padakeria.project.springbootproject.party.dto.PartyRequestDto;
import com.padakeria.project.springbootproject.party.dto.PartyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyService {
    private final PartyRepository partyRepository;
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    public Party createParty(PartyRequestDto partyRequestDto) {
        Account account = accountRepository.findByNickname(partyRequestDto.getOwner());
        Party party = buildParty(partyRequestDto, account);
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

    public void enrollParty(Account account, Party party) {
        Member member = Member.builder()
                .account(account)
                .party(party)
                .point(0)
                .role(MemberRole.TEMPORARY)
                .signupDate(LocalDateTime.now())
                .build();
        member.addParty(party);
        memberRepository.save(member);
        partyRepository.save(party);
    }

    public void cancelEnrollParty(Member member, Party party) {
        member.deleteParty(party);
        memberRepository.delete(member);
    }

    public void secedeParty(Member member, Party party) {
        member.deleteParty(party);
        memberRepository.delete(member);
    }

    public Page<PartyResponseDto> findPagedParty(Integer page) {
        Pageable realPage = PageRequest.of(page <= 0 ? 0 : page - 1, 8, new Sort(Sort.Direction.DESC, "creation"));
        Page<Party> parties = partyRepository.findAll(realPage);

        long totalElements = parties.getTotalElements();
        return new PageImpl<>(parties.stream().map(PartyResponseDto::new).collect(Collectors.toList()), realPage, totalElements);
    }


    private Party buildParty(PartyRequestDto partyRequestDto, Account account) {
        return Party.builder()
                .owner(account)
                .name(partyRequestDto.getName())
                .location(partyRequestDto.getLocation())
                .creation(LocalDateTime.now())
                .subject(partyRequestDto.getSubject())
                .description(partyRequestDto.getDescription())
                .profileImage(partyRequestDto.getProfileImage())
                .build();
    }

    public void acceptMember(Member currentMember) {
        currentMember.changeRole(MemberRole.USER);
    }

    public void banMember(Member member, Party party) {
        if (member.isOwner())
            throw new MemberChangeException(member.getParty().getId(), "모임의 관리자는 탈퇴될 수 없습니다.");

        member.deleteParty(party);
        memberRepository.delete(member);
    }

    public void changeRole(Member member, MemberRole role) {
        if (member.isOwner()) {
            throw new MemberChangeException(member.getParty().getId(), "모임의 관리자는 해당 기능으로 변경 될 수 없습니다.");
        }
        if (member.isTemporaryMember()) {
            throw new MemberChangeException(member.getParty().getId(), "가입 신청 멤버는 해당 기능으로 등급을 변경 할 수 없습니다.");
        }
        if (role == MemberRole.OWNER) {
            throw new MemberChangeException(member.getParty().getId(), "모임의 관리자는 관리자 위임으로만 가능합니다.");
        }

        member.changeRole(role);
        memberRepository.save(member);
    }
}
package com.padakeria.project.springbootproject.party.service;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.member.domain.Member;
import com.padakeria.project.springbootproject.member.domain.MemberRepository;
import com.padakeria.project.springbootproject.member.service.MemberService;
import com.padakeria.project.springbootproject.party.domain.Party;
import com.padakeria.project.springbootproject.party.domain.PartyRepository;
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
    private final MemberService memberService;

    public Party createParty(PartyRequestDto partyRequestDto) {
        Account account = accountRepository.findByNickname(partyRequestDto.getOwner());
        Party party = buildParty(partyRequestDto, account);

        memberService.signupParty(account, party);
        return partyRepository.save(party);
    }

    public Page<PartyResponseDto> findPagedParty(Integer page) {
        Pageable realPage = PageRequest.of(page <= 0 ? 0 : page - 1, 8, new Sort(Sort.Direction.DESC, "creation"));
        Page<Party> parties = partyRepository.findAll(realPage);

        long totalElements = parties.getTotalElements();
        return new PageImpl<>(parties.stream().map(PartyResponseDto::new).collect(Collectors.toList()), realPage, totalElements);
    }

    public Party findPartyById(Long id) {
            return partyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 모임이 없습니다."));
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
}
package com.padakeria.project.springbootproject.party.event;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.party.domain.Member;
import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Async
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberEventListener {

    private Logger logger = LoggerFactory.getLogger("MemberEventListener");

    @EventListener
    public void handlePartyAcceptedByMember(MemberAcceptEvent memberEvent) {
        Member member = memberEvent.getMember();
        Account account = member.getAccount();
        Party party = member.getParty();


        // TODO: 2020-04-14 알림 기능 구현 후
        if (account.isPartyEnrollmentByEmail()) {
            logger.info(party.getId() + "에 " + account.getId() + "가 가입 허가됨");
        }
        if (account.isPartyEnrollmentByWeb()) {
            logger.info(party.getId() + "에 " + account.getId() + "가 가입 허가됨");
        }
    }

    @EventListener
    public void handleEnrollmentNotificationToPartyOwner(MemberCreateEvent memberEvent) {
        Member member = memberEvent.getMember();
        Party party = member.getParty();
        Account owner = party.getOwner();

        if (owner.isPartyEnrollmentByEmail()) {
            logger.info(party.getId() + "에 가입신청이 있습니다 " + owner.getNickname() + "님에게");
        }
        if (owner.isPartyEnrollmentByWeb()) {
            logger.info(party.getId() + "에 가입신청이 있습니다 " + owner.getNickname() + "님에게");
        }
    }

    @EventListener
    public void handleMemberUpdate(MemberUpdateEvent memberEvent) {
        Member member = memberEvent.getMember();
        Account account = member.getAccount();

        if (account.isPartyUpdatedByEmail()) {
            logger.info(memberEvent.getMessage());
        }
        if (account.isPartyUpdatedByWeb()) {
            logger.info(memberEvent.getMessage());
        }
    }
}

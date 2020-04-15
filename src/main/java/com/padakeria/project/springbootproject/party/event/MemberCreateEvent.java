package com.padakeria.project.springbootproject.party.event;

import com.padakeria.project.springbootproject.party.domain.Member;


public class MemberCreateEvent extends MemberEvent {
    public MemberCreateEvent(Member member) {
        super(member, member.getAccount().getNickname() + "이 가입을 신청했습니다");
    }
}

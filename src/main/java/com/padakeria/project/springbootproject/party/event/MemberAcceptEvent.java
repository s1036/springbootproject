package com.padakeria.project.springbootproject.party.event;

import com.padakeria.project.springbootproject.party.domain.Member;

public class MemberAcceptEvent extends MemberEvent {
    public MemberAcceptEvent(Member member) {
        super(member, member.getParty().getName()+"에 대한 가입 신청이 허가 되었습니다");
    }
}

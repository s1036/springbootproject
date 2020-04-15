package com.padakeria.project.springbootproject.party.event;

import com.padakeria.project.springbootproject.party.domain.Member;

public class MemberUpdateEvent extends MemberEvent{
    public MemberUpdateEvent(Member member, String message) {
        super(member, message);
    }
}

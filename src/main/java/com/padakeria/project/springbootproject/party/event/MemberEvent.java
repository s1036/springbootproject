package com.padakeria.project.springbootproject.party.event;

import com.padakeria.project.springbootproject.party.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class MemberEvent {
    protected final Member member;
    protected final String message;
}

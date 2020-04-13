package com.padakeria.project.springbootproject.common.exceptions;

import com.padakeria.project.springbootproject.party.domain.Party;
import lombok.Getter;

@Getter
public class MemberChangeException extends RuntimeException {
    Long partyId;

    public MemberChangeException(Long partyId, String message) {
        super(message);
        this.partyId = partyId;
    }
}

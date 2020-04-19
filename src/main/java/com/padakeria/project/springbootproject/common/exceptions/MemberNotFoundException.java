package com.padakeria.project.springbootproject.common.exceptions;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("모임의 멤버가 아닙니다.");
    }
}

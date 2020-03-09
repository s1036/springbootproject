package com.padakeria.project.springbootproject.common.exceptions;

import org.springframework.security.oauth2.core.OAuth2Error;

public class UserDuplicationException extends RuntimeException {
    public UserDuplicationException(OAuth2Error oAuth2Error, String s) {
    }
}

package com.padakeria.project.springbootproject.common;

import com.padakeria.project.springbootproject.accounts.AccountRole;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Set<AccountRole> accountRoles = new HashSet<>();
        accountRoles.add(AccountRole.USER);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", withMockCustomUser.username());
        attributes.put("email", withMockCustomUser.email());
        attributes.put("sub", "sub");

        Set<SimpleGrantedAuthority> authorities = accountRoles.stream().map(accountRole -> new SimpleGrantedAuthority(accountRole.getKey())).collect(Collectors.toSet());
        
        OAuth2User oAuth2User = new DefaultOAuth2User(authorities
                , attributes, "sub");

        Authentication authentication = new OAuth2AuthenticationToken(oAuth2User,
                authorities,
                withMockCustomUser.authorizedClientRegistrationId());
        context.setAuthentication(authentication);

        return context;
    }
}

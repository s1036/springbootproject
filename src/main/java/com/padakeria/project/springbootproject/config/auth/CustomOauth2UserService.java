package com.padakeria.project.springbootproject.config.auth;

import com.padakeria.project.springbootproject.accounts.Account;
import com.padakeria.project.springbootproject.accounts.AccountRepository;
import com.padakeria.project.springbootproject.common.exceptions.UserDuplicationException;
import com.padakeria.project.springbootproject.config.auth.dto.OauthAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        String registraionId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().
                        getUserInfoEndpoint().
                        getUserNameAttributeName();
        OauthAttributes attributes = OauthAttributes.of(registraionId, userNameAttributeName, oAuth2User.getAttributes());
        Account account = saveOrUpdate(attributes);

        return new DefaultOAuth2User(account.getRoles().stream().map(accountRole -> new SimpleGrantedAuthority(accountRole.getKey())).collect(Collectors.toSet())
                , attributes.getAttributes(), attributes.getUserNameAttributeName());
    }

    private Account saveOrUpdate(OauthAttributes attributes) {
        Account account = accountRepository.findByEmail(attributes.getEmail())
                .orElseGet(attributes::toEntity);

        account = Optional.of(account)
                .filter(dbAccount -> attributes.getRegistraionId().equals(dbAccount.getRegistrationId()))
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElseThrow(() -> new UserDuplicationException(new OAuth2Error("500"), "이미 다른 경로로 가입된 이메일입니다."));

        return accountRepository.save(account);
    }
}

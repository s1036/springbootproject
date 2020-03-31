package com.padakeria.project.springbootproject.account.validator;

import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.account.dto.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameFormValidator implements Validator {
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(NicknameForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) object;
        if (accountRepository.existsByNickname(nicknameForm.getNickname())) {
            errors.rejectValue("nickname", "invalid nickname", new Object[]{nicknameForm.getNickname()}, "닉네임이 이미 존재합니다.");
        }
    }
}

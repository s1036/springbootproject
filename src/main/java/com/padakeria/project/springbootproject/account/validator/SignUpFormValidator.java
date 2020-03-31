package com.padakeria.project.springbootproject.account.validator;

import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.account.dto.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) object;
        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid email", new Object[]{signUpForm.getEmail()}, "이메일이 이미 존재합니다.");
        }
        if(accountRepository.existsByNickname(signUpForm.getNickname())){
            errors.rejectValue("nickname", "invalid nickname", new Object[]{signUpForm.getNickname()}, "닉네임이 이미 존재합니다.");
        }
    }
}

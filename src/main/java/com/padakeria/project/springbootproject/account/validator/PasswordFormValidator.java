package com.padakeria.project.springbootproject.account.validator;

import com.padakeria.project.springbootproject.account.dto.PasswordForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PasswordFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {

        return clazz.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) target;
        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())) {
            errors.rejectValue("newPassword","wrongValue", "새 비밀번호가 일치하지 않습니다.");
        }
    }
}

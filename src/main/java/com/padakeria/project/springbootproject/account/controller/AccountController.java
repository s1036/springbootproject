package com.padakeria.project.springbootproject.account.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.CurrentUser;
import com.padakeria.project.springbootproject.account.dto.AccountResponseDto;
import com.padakeria.project.springbootproject.account.dto.SignUpForm;
import com.padakeria.project.springbootproject.account.service.AccountService;
import com.padakeria.project.springbootproject.account.validator.SignUpFormValidator;
import com.padakeria.project.springbootproject.account.validator.group.OrderCheck;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final SignUpFormValidator signUpFormValidator;

    private final ModelMapper modelMapper;

    @InitBinder(value = "signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping(value = "/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping(value = "/sign-up")
    public String signUpSubmit(@Validated(value = OrderCheck.class) SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        Account account = accountService.signUpProcess(signUpForm);
        accountService.signUpEmailConfirm(account);
        accountService.loginProcess(account);

        return "redirect:/";
    }

    @GetMapping(value = "/check-email-token")
    public String checkEmailToken(@RequestParam String token, @RequestParam String email, Model model) {
        if (token.isEmpty() || email.isEmpty()) {
            model.addAttribute("error", "wrong url");
            return "account/sign-up-check";
        }

        boolean isVerified = accountService.signUpVerify(token, email);

        if (!isVerified) {
            model.addAttribute("error", "잘못된 인증입니다.");
            return "account/sign-up-check";
        }

        Account account = accountService.getAccountByEmail(email);
        accountService.loginProcess(account);

        return "account/sign-up-check";
    }


    @GetMapping(value = "/check-email")
    public String checkEmail(@CurrentUser Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    @GetMapping(value = "/check-email-resend")
    public String checkEmailResend(@CurrentUser Account account, Model model) {
        if (!account.isEmailVerified() && !account.canSendConfirmEmail()) {
            model.addAttribute("error", "인증 메일은 1시간에 한번만 보낼 수 있습니다.");
            return "account/check-email";
        }

        accountService.emailConfirmResend(account);
        return "redirect:/";
    }

    @GetMapping(value = "/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, Model model, @CurrentUser Account account) {
        Account byNickname = accountService.getAccountByNickname(nickname);
        if (byNickname == null) {
            throw new IllegalArgumentException(nickname + "은 없는 유저입니다");
        }

        AccountResponseDto accountResponseDto = modelMapper.map(byNickname,AccountResponseDto.class);

        model.addAttribute("account", accountResponseDto);
        model.addAttribute("isOwner", nickname.equals(account.getNickname()));

        return "account/profile";
    }
}

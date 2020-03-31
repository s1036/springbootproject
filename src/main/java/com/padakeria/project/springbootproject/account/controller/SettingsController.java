package com.padakeria.project.springbootproject.account.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.CurrentUser;
import com.padakeria.project.springbootproject.account.dto.*;
import com.padakeria.project.springbootproject.account.service.AccountService;
import com.padakeria.project.springbootproject.account.validator.NicknameFormValidator;
import com.padakeria.project.springbootproject.account.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SettingsController {
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final NicknameFormValidator nicknameFormValidator;


    @InitBinder(value = "passwordForm")
    public void passwordFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder(value = "nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameFormValidator);
    }


    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model) {

        model.addAttribute("account", modelMapper.map(account, AccountResponseDto.class));
        model.addAttribute("profile", modelMapper.map(account, Profile.class));
        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String profileUpdate(@CurrentUser Account account, @Valid Profile profile, Errors errors, RedirectAttributes redirectAttributes, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            return "settings/profile";
        }

        accountService.updateProfile(account, profile);

        redirectAttributes.addFlashAttribute("message", "수정 완료");
        return "redirect:/settings/profile";
    }

    @GetMapping("/settings/password")
    public String passwordUpdateForm(@CurrentUser Account account, Model model) {
        model.addAttribute("account", modelMapper.map(account, AccountResponseDto.class));
        model.addAttribute("passwordForm", new PasswordForm());
        return "settings/password";
    }

    @PostMapping("/settings/password")
    public String passwordUpdate(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors, RedirectAttributes redirectAttributes, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", modelMapper.map(account, AccountResponseDto.class));
            return "settings/password";
        }
        accountService.updatePassword(account, passwordForm.getNewPassword());
        redirectAttributes.addFlashAttribute("message", "수정 완료");

        return "redirect:/settings/password";
    }

    @GetMapping("/settings/notifications")
    public String notificationsUpdate(@CurrentUser Account account, Model model) {
        model.addAttribute("notificationsForm", modelMapper.map(account, NotificationsForm.class));
        return "settings/notifications";
    }

    @PostMapping("/settings/notifications")
    public String notificationsUpdate(@CurrentUser Account account, @Valid NotificationsForm notificationsForm, Errors errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "settings/notifications";
        }
        accountService.updateNotifications(account, notificationsForm);
        redirectAttributes.addFlashAttribute("message", "수정 완료");

        return "redirect:/settings/notifications";
    }

    @GetMapping("/settings/account")
    public String accountForm(@CurrentUser Account account, Model model) {
        model.addAttribute("nicknameForm", modelMapper.map(account, NicknameForm.class));
        return "settings/account";
    }

    @PostMapping("/settings/account")
    public String accountUpdate(@CurrentUser Account account, @Valid NicknameForm nicknameForm, Errors errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "settings/account";
        }

        String nickname = nicknameForm.getNickname();
        accountService.updateNickname(account, nickname);
        accountService.loginProcess(account);
        redirectAttributes.addFlashAttribute("message", "수정 완료");

        return "redirect:/settings/account";
    }
}

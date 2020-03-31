package com.padakeria.project.springbootproject.common.controller;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.CurrentUser;
import com.padakeria.project.springbootproject.account.dto.AccountResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ModelMapper modelMapper;

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute("account", modelMapper.map(account, AccountResponseDto.class));
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}

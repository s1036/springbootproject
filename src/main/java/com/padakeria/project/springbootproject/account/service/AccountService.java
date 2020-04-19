package com.padakeria.project.springbootproject.account.service;

import com.padakeria.project.springbootproject.account.domain.Account;
import com.padakeria.project.springbootproject.account.domain.AccountRepository;
import com.padakeria.project.springbootproject.account.dto.NotificationsForm;
import com.padakeria.project.springbootproject.account.dto.Profile;
import com.padakeria.project.springbootproject.account.dto.SignUpForm;
import com.padakeria.project.springbootproject.account.dto.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    public Account signUpProcess(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        return newAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword())
                .partyCreatedByWeb(true)
                .partyEnrollmentByWeb(true)
                .partyUpdatedByWeb(true)
                .build();
        return accountRepository.save(account);
    }


    public void signUpEmailConfirm(Account newAccount) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(newAccount.getEmail());
        simpleMailMessage.setSubject("회원 가입 인증");
        simpleMailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    public void emailConfirmResend(Account account) {
        account.generateEmailCheckToken();
        signUpEmailConfirm(account);
        accountRepository.save(account);
    }


    public boolean signUpVerify(String token, String email) {
        Account account = accountRepository.findByEmail(email);

        if (account == null)
            return false;

        return account.isValidToken(token);
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Account getAccountByNickname(String nickname) {
        return accountRepository.findByNickname(nickname);
    }


    public void loginProcess(Account account) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(new UserAccount(account, authorities), account.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void updateProfile(Account account, Profile profile) {
        Account updatedAccount = account.updateProfile(profile);
        accountRepository.save(updatedAccount);
    }

    public void updatePassword(Account account, String password) {
        Account updatedAccount = account.updatePassword(passwordEncoder.encode(password));
        accountRepository.save(updatedAccount);
    }

    public void updateNotifications(Account account, NotificationsForm notificationsForm) {
        Account updatedAccount = account.updateNotifications(notificationsForm);
        accountRepository.save(updatedAccount);
    }

    public void updateNickname(Account account, String nickname) {
        Account updatedAccount = account.updateNickname(nickname);
        accountRepository.save(updatedAccount);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String nicknameOrEmail)  {
        Account loginAccount = accountRepository.findByEmail(nicknameOrEmail);
        if (loginAccount == null) {
            loginAccount = accountRepository.findByNickname(nicknameOrEmail);
        }
        if (loginAccount == null) {
            throw new UsernameNotFoundException(nicknameOrEmail);
        }
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UserAccount(loginAccount, authorities);
    }
}

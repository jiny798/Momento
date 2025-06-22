package jiny.futurevia.service.modules.account.application;

import jiny.futurevia.service.modules.account.domain.dto.AccountContext;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.domain.entity.Role;
import jiny.futurevia.service.modules.account.endpoint.dto.SignUpForm;
import jiny.futurevia.service.modules.account.endpoint.dto.UserResponse;
import jiny.futurevia.service.modules.account.exception.RoleNotFound;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.account.infra.repository.RoleRepository;
import jiny.futurevia.service.infra.config.AppProperties;
import jiny.futurevia.service.infra.mail.EmailMessage;
import jiny.futurevia.service.infra.mail.EmailService;
import jiny.futurevia.service.modules.exception.type.UserNotFound;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service("userDetailsService")
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("[loadUserByUsername]");
        Account account = Optional.ofNullable(accountRepository.findByEmail(email))
                .orElse(accountRepository.findByNickname(email));
        if (account == null) {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }
        List<GrantedAuthority> authorities = account.getAccountRoles()
                .stream()
                .map(accountRole -> accountRole.getRole().getRoleName())
                .collect(Collectors.toSet())
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new AccountContext(account, authorities);
    }

    public Account signUp(SignUpForm signUpForm) {
        Role role = roleRepository.findByRoleName("ROLE_USER").orElseThrow(RoleNotFound::new);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Account account = Account.from(signUpForm.getEmail(),
                signUpForm.getNickname(),
                passwordEncoder.encode(signUpForm.getPassword()),
                roles
        );
        account.generateToken();

        return accountRepository.save(account);
    }

    public void sendVerificationEmail(Account newAccount) {
        log.info("[AccountService] sendVerificationEmail");
        Context context = new Context();
        context.setVariable("link", String.format("/check-email-token?token=%s&email=%s", newAccount.getEmailToken(),
                newAccount.getEmail()));
        context.setVariable("nickname", newAccount.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "Futurevia 가입 인증을 위해 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());

        String message = templateEngine.process("mail/simple-link", context);
        emailService.sendEmail(EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("Futurevia 회원 가입 인증")
                .message(message)
                .build());
    }

    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


    public void verify(Account account) {
        log.info("[AccountService] verify");
        account.verified();
    }

    public void updateNickname(Account account, String nickname) {
        account.updateNickname(nickname);
        accountRepository.save(account);
    }


    public void updatePassword(Account account, String newPassword) {
        account.updatePassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    public void sendLoginLink(Account account) {
        account.generateToken();

        Context context = new Context();
        context.setVariable("link", "/login-by-email?token=" + account.getEmailToken() + "&email=" + account.getEmail());
        context.setVariable("nickname", account.getNickname());
        context.setVariable("linkName", "Futurevia 로그인하기");
        context.setVariable("message", "로그인 하려면 아래 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        emailService.sendEmail(EmailMessage.builder()
                .to(account.getEmail())
                .subject("[Futurevia] 로그인 링크")
                .message(message)
                .build());
    }

    public Account getAccountBy(String nickname) {
        return Optional.ofNullable(accountRepository.findByNickname(nickname))
                .orElseThrow(() -> new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다."));
    }

    public UserResponse getUserProfile(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(UserNotFound::new);
        return new UserResponse(account);
    }
}
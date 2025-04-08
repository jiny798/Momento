package jiny.futurevia.service;

import jiny.futurevia.service.infra.security.token.RestAuthenticationToken;
import jiny.futurevia.service.modules.account.application.AccountService;
import jiny.futurevia.service.modules.account.endpoint.dto.SignUpForm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;

    public WithAccountSecurityContextFactory(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public SecurityContext createSecurityContext(WithAccount annotation) {
        String[] nicknames = annotation.value();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 마지막 계정이 컨트롤러 인자 @CurrentUser 에 전달된다
        for (String nickname : nicknames) {
            SignUpForm signUpForm = new SignUpForm();
            signUpForm.setNickname(nickname);
            signUpForm.setEmail(nickname + "@jiny.com");
            signUpForm.setPassword("jiny1234");
            accountService.signUp(signUpForm);

            UserDetails principal = accountService.loadUserByUsername(nickname);
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            Authentication authentication = new RestAuthenticationToken(authorities, principal, null);
            context.setAuthentication(authentication);
        }
        return context;
    }
}
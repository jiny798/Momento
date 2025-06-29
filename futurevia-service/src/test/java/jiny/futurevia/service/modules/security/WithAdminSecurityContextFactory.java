package jiny.futurevia.service.modules.security;

import jiny.futurevia.service.infra.security.token.RestAuthenticationToken;
import jiny.futurevia.service.modules.account.application.AccountService;
import jiny.futurevia.service.modules.account.domain.dto.AccountContext;
import jiny.futurevia.service.modules.account.endpoint.dto.request.CreateAccountRequest;
import jiny.futurevia.service.modules.account.endpoint.dto.request.SignUpForm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.List;

public class WithAdminSecurityContextFactory implements WithSecurityContextFactory<WithAdmin> {

    private final AccountService accountService;

    public WithAdminSecurityContextFactory(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public SecurityContext createSecurityContext(WithAdmin annotation) {
        String[] nicknames = annotation.value();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 마지막 계정이 컨트롤러 인자 @CurrentUser 에 전달된다
        for (String nickname : nicknames) {
            AccountContext accountContext = null;
            try{
                accountContext = (AccountContext) accountService.loadUserByUsername(nickname);
            }catch (UsernameNotFoundException exception){
                CreateAccountRequest createAccountRequest = new CreateAccountRequest();
                createAccountRequest.setNickname(nickname);
                createAccountRequest.setEmail(nickname + "@jiny.com");
                createAccountRequest.setPassword("jiny1234");
//                accountService.signUpAdmin(signUpForm);
                accountContext = (AccountContext) accountService.loadUserByUsername(nickname);
            }

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            //            Authentication authentication = new RestAuthenticationToken(authorities, principal, null);
            Authentication authentication = new RestAuthenticationToken(authorities, accountContext.getAccount(), null);

            context.setAuthentication(authentication);
        }
        return context;
    }
}
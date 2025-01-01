package jiny.futurevia.service.account.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.account.domain.entity.Role;
import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {
	/*** Authenticate ***/
	private final AuthenticationManager authenticationManager;
	private final HttpSessionSecurityContextRepository sessionContextRepository = new HttpSessionSecurityContextRepository();
	private final AccountService accountService;

	@Transactional
	public void login(Account loginAccount, HttpServletRequest request, HttpServletResponse response) {
		log.info("[LoginService] login");
		Set<Role> set = loginAccount.getUserRoles();
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		for (Role role : set) {
			System.out.println("role : " + role.getRoleName());
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}

		UsernamePasswordAuthenticationToken authenticatedToken = new UsernamePasswordAuthenticationToken(
			accountService.loadUserByUsername(loginAccount.getEmail()),
			null,
			authorities);

		SecurityContext securityContext = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
		securityContext.setAuthentication(authenticatedToken);

		// Save in ThreadLocal
		SecurityContextHolder.getContextHolderStrategy().setContext(securityContext);
		// Save in Session
		sessionContextRepository.saveContext(securityContext, request, response);

	}
}

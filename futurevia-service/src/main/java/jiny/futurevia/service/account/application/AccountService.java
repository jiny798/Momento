package jiny.futurevia.service.account.application;

import jiny.futurevia.service.account.domain.dto.AccountContext;
import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.account.domain.entity.Role;
import jiny.futurevia.service.account.endpoint.controller.dto.NotificationForm;
import jiny.futurevia.service.account.endpoint.controller.dto.ProfileDto;
import jiny.futurevia.service.account.endpoint.controller.dto.SignUpForm;
import jiny.futurevia.service.account.repository.AccountRepository;
import jiny.futurevia.service.admin.repository.RoleRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class AccountService implements UserDetailsService{

	private final AccountRepository accountRepository;
	private final RoleRepository roleRepository;
	private final JavaMailSender mailSender;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Account account = Optional.ofNullable(accountRepository.findByEmail(email))
			.orElse(accountRepository.findByNickname(email));
		if (account == null) {
			throw new UsernameNotFoundException("No user found with email: " + email);
		}
		List<GrantedAuthority> authorities = account.getUserRoles()
			.stream()
			.map(Role::getRoleName)
			.collect(Collectors.toSet())
			.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		ModelMapper mapper = new ModelMapper();

		return new AccountContext(account, authorities);
	}

	@Transactional
	public Account signUp(SignUpForm signUpForm) {
		Account newAccount = saveNewAccount(signUpForm);
		newAccount.generateToken();
		sendVerificationEmail(newAccount);
		return newAccount;
	}

	private Account saveNewAccount(SignUpForm signUpForm) {
		Role role = roleRepository.findByRoleName("ROLE_USER");
		Set<Role> roles = new HashSet<>();
		roles.add(role);

		Account account = Account.builder()
			.email(signUpForm.getEmail())
			.nickname(signUpForm.getNickname())
			.password(passwordEncoder.encode(signUpForm.getPassword()))
			.notificationSetting(Account.NotificationSetting.builder()
				.studyCreatedByWeb(true)
				.studyUpdatedByWeb(true)
				.studyRegistrationResultByWeb(true)
				.build())
			.userRoles(roles)
			.build();
		return accountRepository.save(account);
	}

	public void sendVerificationEmail(Account newAccount) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(newAccount.getEmail());
		mailMessage.setSubject("회원 가입 인증");
		mailMessage.setText(String.format("/check-email-token?token=%s&email=%s", newAccount.getEmailToken(),
			newAccount.getEmail()));
		mailSender.send(mailMessage);
	}

	public Account findAccountByEmail(String email) {
		return accountRepository.findByEmail(email);
	}


	@Transactional
	public void verify(Account account) {
		log.info("[AccountService] verify");
		account.verified();
	}


	@Transactional
	public void updateProfile(Account account, ProfileDto profile) {
		account.updateProfile(profile);
		accountRepository.save(account);
	}

	public void updateNotification(Account account, NotificationForm notificationForm) {
		account.updateNotification(notificationForm);
		accountRepository.save(account);
	}

	public void updateNickname(Account account, String nickname) {
		account.updateNickname(nickname);
		accountRepository.save(account);
	}
}
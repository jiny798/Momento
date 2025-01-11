package jiny.futurevia.service.account.application;

import jiny.futurevia.service.account.domain.dto.AccountContext;
import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.account.domain.entity.Role;
import jiny.futurevia.service.account.domain.entity.Zone;
import jiny.futurevia.service.account.endpoint.controller.dto.NotificationForm;
import jiny.futurevia.service.account.endpoint.controller.dto.ProfileDto;
import jiny.futurevia.service.account.endpoint.controller.dto.SignUpForm;
import jiny.futurevia.service.account.infra.repository.AccountRepository;
import jiny.futurevia.service.admin.repository.RoleRepository;
import jiny.futurevia.service.mail.EmailMessage;
import jiny.futurevia.service.mail.EmailService;
import jiny.futurevia.service.tag.domain.entity.Tag;
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
@Transactional
@Service("userDetailsService")
public class AccountService implements UserDetailsService{

	private final AccountRepository accountRepository;
	private final RoleRepository roleRepository;

	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	@Override
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


	public Account signUp(SignUpForm signUpForm) {
		Account newAccount = saveNewAccount(signUpForm);
		sendVerificationEmail(newAccount);
		return newAccount;
	}

	private Account saveNewAccount(SignUpForm signUpForm) {
		Role role = roleRepository.findByRoleName("ROLE_USER");
		Set<Role> roles = new HashSet<>();
		roles.add(role);

		Account account = Account.from(signUpForm.getEmail(),
				signUpForm.getNickname(),
				passwordEncoder.encode(signUpForm.getPassword()),
				roles);

		account.generateToken();
		return accountRepository.save(account);
	}

	public void sendVerificationEmail(Account newAccount) {
		log.info("[AccountService] sendVerificationEmail");
		log.info("[AccountService] emailService {}", emailService );
		emailService.sendEmail(EmailMessage.builder()
				.to(newAccount.getEmail())
				.subject("Futurevia 회원 가입 인증")
				.message(String.format("/check-email-token?token=%s&email=%s", newAccount.getEmailToken(),
						newAccount.getEmail()))
				.build());
	}

	public Account findAccountByEmail(String email) {
		return accountRepository.findByEmail(email);
	}



	public void verify(Account account) {
		log.info("[AccountService] verify");
		account.verified();
	}



	public void updateProfile(Account account, ProfileDto profile) {
		log.info("[update profile]");
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


	public void updatePassword(Account account, String newPassword) {
		account.updatePassword(passwordEncoder.encode(newPassword));
		accountRepository.save(account);
	}

	public void sendLoginLink(Account account) {
		account.generateToken();
		emailService.sendEmail(EmailMessage.builder()
				.to(account.getEmail())
				.subject("[Futurevia] 로그인 링크")
				.message("/login-by-email?token=" + account.getEmailToken() + "&email=" + account.getEmail())
				.build());
	}

	public void addTag(Account account, Tag tag) {
		accountRepository.findById(account.getId())
				.ifPresent(a -> a.getTags().add(tag));
	}

	public Set<Tag> getTags(Account account) {
		return accountRepository.findById(account.getId()).orElseThrow().getTags();
	}

	public void removeTag(Account account, Tag tag) {
		accountRepository.findById(account.getId())
				.map(Account::getTags)
				.ifPresent(tags -> tags.remove(tag));
	}

	public Set<Zone> getZones(Account account) {
		return accountRepository.findById(account.getId())
				.orElseThrow()
				.getZones();
	}

	public void addZone(Account account, Zone zone) {
		accountRepository.findById(account.getId())
				.ifPresent(a -> a.getZones().add(zone));
	}

	public void removeZone(Account account, Zone zone) {
		accountRepository.findById(account.getId())
				.ifPresent(a -> a.getZones().remove(zone));
	}
}
package jiny.futurevia.service.account.endpoint.controller;

import jiny.futurevia.service.account.application.AccountService;
import jiny.futurevia.service.account.domain.entity.Account;

import jiny.futurevia.service.account.endpoint.controller.dto.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jiny.futurevia.service.account.repository.AccountRepository;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	AccountService accountService;

	@BeforeEach
	void beforeEach() {
		SignUpForm signUpForm = new SignUpForm();
		signUpForm.setNickname("jiny1234");
		signUpForm.setEmail("jiny1234@jiny.com");
		signUpForm.setPassword("jiny1234");
		accountService.signUp(signUpForm);
	}

	@AfterEach
	void afterEach() {
		accountRepository.deleteAll();
	}


	@Test
	@DisplayName("회원 가입 화면 진입")
	void signUpForm() throws Exception {
		mockMvc.perform(get("/sign-up"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andExpect(model().attributeExists("signUpForm"))
			.andExpect(unauthenticated());

	}

	@Test
	@DisplayName("회원 가입 오류")
	void signUpSubmitWithError() throws Exception {
		mockMvc.perform(post("/sign-up")
				.param("nickname", "nickname")
				.param("email", "email@email") // 이메일 오류
				.param("password", "1234!67") // 비밀번호 8자리 미만
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"));
	}

	@Test
	@DisplayName("회원 가입 정상 처리 : 회원 저장")
	void signUpSubmit() throws Exception {

		String password = "abcd1234!@";
		mockMvc.perform(post("/sign-up")
				.param("nickname", "nickname")
				.param("email", "dareme798@naver.com")
				.param("password", password)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"));
		//                .andExpect(authenticated().withUsername("nickname"));

		Assertions.assertTrue(accountRepository.existsByEmail("dareme798@naver.com"));

		Account account = accountRepository.findByEmail("dareme798@naver.com");
		Assertions.assertNotEquals(account.getPassword(), password);
	}

	@DisplayName("인증 메일 확인: 잘못된 링크")
	@Test
	void verifyEmailWithWrongLink() throws Exception {
		mockMvc.perform(get("/check-email-token")
				.param("token", "token")
				.param("email", "email"))
			.andExpect(status().isOk())
			.andExpect(view().name("account/email-verification"))
			.andExpect(model().attributeExists("error"));
	}

	@DisplayName("인증 메일 확인: 유효한 링크")
	@Test
	@Transactional
	void verifyEmail() throws Exception {
		Account newAccount = accountRepository.findByEmail("jiny1234@jiny.com");
		mockMvc.perform(get("/check-email-token")
				.param("token", newAccount.getEmailToken())
				.param("email", newAccount.getEmail()))
			.andExpect(status().isOk())
			.andExpect(view().name("account/email-verification"))
			.andExpect(model().attributeDoesNotExist("error"))
			.andExpect(model().attributeExists("numberOfUsers", "nickname"));
		//                .andExpect(authenticated().withUsername("nickname"));
	}

	@Test
	@DisplayName("이메일 로그인 정상 처리")
	void login_with_email() throws Exception {
		mockMvc.perform(post("/login")
				.param("username", "jiny1234@jiny.com")
				.param("password", "jiny1234")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(authenticated().withUsername("jiny1234"));
	}


	@Test
	@DisplayName("닉네임 로그인 정상 처리")
	void login_with_nickname() throws Exception {
		mockMvc.perform(post("/login")
				.param("username", "jiny1234")
				.param("password", "jiny1234")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(authenticated().withUsername("jiny1234"));
	}

	@Test
	@DisplayName("로그인 실패")
	void login_fail() throws Exception {
		mockMvc.perform(post("/login")
				.param("username", "jiny1234")
				.param("password", "jiny12345")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?error"))
			.andExpect(unauthenticated());
	}

	@Test
	@DisplayName("로그아웃")
	void logout() throws Exception {
		mockMvc.perform(post("/logout")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?logout"))
			.andExpect(unauthenticated());
	}

}

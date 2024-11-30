package jiny.futurevia.service.account.endpoint.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jiny.futurevia.service.account.repository.AccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	AccountRepository accountRepository;
	@MockBean
	JavaMailSender mailSender;

	@Test
	@DisplayName("회원 가입 화면 진입")
	void signUpForm() throws Exception {
		mockMvc.perform(get("/sign-up"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("account/sign-up"))
			.andExpect(model().attributeExists("signUpForm"));
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
		mockMvc.perform(post("/sign-up")
			.param("nickname", "nickname")
			.param("email", "dareme798@naver.com")
			.param("password", "1234!@#$")
			.with(csrf()))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"));

		Assertions.assertTrue(accountRepository.existsByEmail("dareme798@naver.com"));

		then(mailSender)
			.should()
			.send(any(SimpleMailMessage.class));
	}
}

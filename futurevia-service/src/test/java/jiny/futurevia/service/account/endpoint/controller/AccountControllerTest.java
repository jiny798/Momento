package jiny.futurevia.service.account.endpoint.controller;

import jiny.futurevia.service.account.domain.entity.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
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
        Account account = Account.builder()
                .email("email@email.com")
                .password("1234!@#$")
                .nickname("nickname")
                .notificationSetting(Account.NotificationSetting.builder()
                        .studyCreatedByWeb(true)
                        .studyUpdatedByWeb(true)
                        .studyRegistrationResultByWeb(true)
                        .build())
                .build();
        Account newAccount = accountRepository.save(account);
        newAccount.generateToken();
        mockMvc.perform(get("/check-email-token")
                        .param("token", newAccount.getEmailToken())
                        .param("email", newAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/email-verification"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("numberOfUsers", "nickname"));
//                .andExpect(authenticated().withUsername("nickname"));
    }
}

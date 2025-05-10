package jiny.futurevia.service.modules.account.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jiny.futurevia.service.infra.IntegrationTest;
import jiny.futurevia.service.modules.account.application.AccountService;
import jiny.futurevia.service.modules.account.domain.entity.Account;

import jiny.futurevia.service.infra.mail.EmailMessage;
import jiny.futurevia.service.infra.mail.EmailService;
import jiny.futurevia.service.modules.account.endpoint.dto.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;

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

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @AfterEach
    void setUpAfterTest() {
        accountRepository.deleteAll();
    }

	@Test
	@DisplayName("회원 가입 : 정상처리")
	void signUpSubmitWithError() throws Exception {
        // given
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("nickname2");
        signUpForm.setEmail("email2@test.test");
        signUpForm.setPassword("password2");
        String signUpData = mapper.writeValueAsString(signUpForm);

        // when
		mockMvc.perform(post("/api/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(signUpData))
			.andExpect(status().isOk())
			.andDo(print());

        //then
        assertEquals(1, accountRepository.count());
        Account account = accountRepository.findByEmail(signUpForm.getEmail());
        assertEquals(account.getNickname(), signUpForm.getNickname());
        assertEquals(account.getEmail(), signUpForm.getEmail());

	}

}

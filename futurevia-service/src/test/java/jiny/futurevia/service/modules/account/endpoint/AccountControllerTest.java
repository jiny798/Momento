package jiny.futurevia.service.modules.account.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jiny.futurevia.service.modules.account.application.AccountService;
import jiny.futurevia.service.modules.account.domain.entity.Account;

import jiny.futurevia.service.modules.account.endpoint.dto.request.CreateAccountRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;

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
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setNickname("nickname2");
        createAccountRequest.setEmail("email2@test.test");
        createAccountRequest.setPassword("password2");
        String signUpData = mapper.writeValueAsString(createAccountRequest);

        // when
		mockMvc.perform(post("/api/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(signUpData))
			.andExpect(status().isOk())
			.andDo(print());

        //then
        assertEquals(1, accountRepository.count());
        Account account = accountRepository.findByEmail(createAccountRequest.getEmail());
        assertEquals(account.getNickname(), createAccountRequest.getNickname());
        assertEquals(account.getEmail(), createAccountRequest.getEmail());

	}

}

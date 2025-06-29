package jiny.futurevia.service.modules.account.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jiny.futurevia.service.modules.account.application.AccountService;
import jiny.futurevia.service.modules.account.application.LoginService;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.endpoint.dto.request.CreateAccountRequest;
import jiny.futurevia.service.modules.account.endpoint.dto.response.UserResponse;
import jiny.futurevia.service.modules.account.endpoint.validator.SignUpFormValidator;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final String ERROR_ALREADY_LOGOUT_STATUS = "VALIDATE_ERROR";

    @Qualifier("userDetailsService")
    private final AccountService accountService;
    private final LoginService loginService;
    private final SignUpFormValidator signUpFormValidator;
    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> signUpSubmit(@Valid @RequestBody CreateAccountRequest createAccountRequest, Errors errors, HttpServletRequest request,
                                                  HttpServletResponse response) {
        UserResponse userResponse = accountService.signUp(createAccountRequest);
        return ApiResponse.success(userResponse);
    }

    @GetMapping
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ApiResponse.success();
    }

    @GetMapping("/users/me")
    public ApiResponse<UserResponse> getMe(@AuthenticationPrincipal Account account) {
        if (account == null) {
            throw new AuthenticationCredentialsNotFoundException("Login required");
        }
        UserResponse userResponse = accountService.getUserProfile(account.getId());
        return ApiResponse.success(userResponse);
    }

}
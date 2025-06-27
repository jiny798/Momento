package jiny.futurevia.service.modules.account.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jiny.futurevia.service.modules.account.application.AccountService;
import jiny.futurevia.service.modules.account.application.LoginService;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.endpoint.dto.SignUpForm;
import jiny.futurevia.service.modules.account.endpoint.dto.UserResponse;
import jiny.futurevia.service.modules.account.endpoint.validator.SignUpFormValidator;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.account.support.CurrentUser;
import jiny.futurevia.service.modules.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ApiResponse<Void> signUpSubmit(@Valid @RequestBody SignUpForm signUpForm, Errors errors, HttpServletRequest request,
                                          HttpServletResponse response) {
        Account account = accountService.signUp(signUpForm);
        return ApiResponse.success();
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
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
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Qualifier("userDetailsService")
    private final AccountService accountService;
    private final LoginService loginService;
    private final SignUpFormValidator signUpFormValidator;
    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/test-account")
    public void testAccount() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail("abc798@gmail.com");
        signUpForm.setPassword("abc798!");
        signUpForm.setNickname("abc798");
        Account account = accountService.signUpAdmin(signUpForm);
        accountService.verify(account);
    }

    @PostMapping("/sign-up")
    public void signUpSubmit(@Valid @RequestBody SignUpForm signUpForm, Errors errors, HttpServletRequest request,
                               HttpServletResponse response) {
        Account account = accountService.signUpUser(signUpForm);
    }

    // 메일 인증 임시 주석
    /*
    @GetMapping("/check-email-token")
    public String verifyEmail(String token, String email, Model model, HttpServletRequest request, HttpServletResponse response) { // HttpServletRequest request,	HttpServletResponse response

        log.info("[check-email-token] verifyEmail");
        Account account = accountService.findAccountByEmail(email);
        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return "account/email-verification";
        }

        
        if (!token.equals(account.getEmailToken())) {
            model.addAttribute("error", "wrong.token");
            return "account/email-verification";
        }
        accountService.verify(account);
		loginService.login(account, request, response);

        model.addAttribute("numberOfUsers", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return "account/email-verification";
    }

    @GetMapping("/check-email")
    public String checkMail(@CurrentUser Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }


    @GetMapping("/resend-email")
    public String resendEmail(@CurrentUser Account account, Model model) {
        if (!account.enableToSendEmail()) {
            model.addAttribute("error", "인증 이메일은 5분에 한 번만 전송할 수 있습니다.");
            model.addAttribute("email", account.getEmail());
            return "account/check-email";
        }
        accountService.sendVerificationEmail(account);
        return "redirect:/";
    }

    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, Model model, @CurrentUser Account account) {
        log.info("viewProfile ");
        Account byNickname = accountService.getAccountBy(nickname);
        if (byNickname == null) {
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }
        if(account != null){
            log.info("viewProfile account != null");
            model.addAttribute(account);
        }
        model.addAttribute(byNickname);
        model.addAttribute("isOwner", byNickname.equals(account));
        return "account/profile";
    }

    @GetMapping("/email-login")
    public String emailLoginForm() {
        return "account/email-login";
    }

    @PostMapping("/email-login")
    public String sendLinkForEmailLogin(String email, Model model) { // RedirectAttributes attributes
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
            return "account/email-login";
        }
//        if (!account.enableToSendEmail()) {
//            model.addAttribute("error", "잠시 후 다시 시도해주세요.");
//            return "account/email-login";
//        }
        accountService.sendLoginLink(account);
//        attributes.addFlashAttribute("message", "로그인을 위한 링크를 이메일로 전송하였습니다.");
        return "redirect:/email-login";
    }

    @GetMapping("/login-by-email")
    public String loginByEmail(String token, String email, Model model, HttpServletRequest request, HttpServletResponse response) {
        Account account = accountRepository.findByEmail(email);
        if (account == null || !account.isValid(token)) {
            model.addAttribute("error", "로그인할 수 없습니다.");
            return "account/logged-in-by-email";
        }
        loginService.login(account, request, response);
        return "account/logged-in-by-email";
    }

     */

    @GetMapping
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal Account account) {
        if (account == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserResponse userResponse = accountService.getUserProfile(account.getId());
        return ResponseEntity.ok().body(userResponse);
    }

}
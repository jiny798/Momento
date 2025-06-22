package jiny.futurevia.service.modules.account.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jiny.futurevia.service.modules.account.application.AccountService;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.endpoint.dto.*;
import jiny.futurevia.service.modules.account.endpoint.validator.NicknameFormValidator;
import jiny.futurevia.service.modules.account.endpoint.validator.PasswordFormValidator;
import jiny.futurevia.service.modules.account.support.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
public class SettingsController {
    private final AccountService accountService;
    private final NicknameFormValidator nicknameFormValidator;
    private final ObjectMapper objectMapper;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameFormValidator);
    }

    public void updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors, Model model) { // RedirectAttributes attributes
        accountService.updatePassword(account, passwordForm.getNewPassword());
    }

    public void nicknameForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new NicknameForm(account.getNickname()));
    }

}
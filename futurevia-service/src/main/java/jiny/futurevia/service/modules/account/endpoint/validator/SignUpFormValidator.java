package jiny.futurevia.service.modules.account.endpoint.validator;

import jiny.futurevia.service.modules.account.endpoint.dto.request.CreateAccountRequest;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) { // (3)
        return clazz.isAssignableFrom(CreateAccountRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateAccountRequest createAccountRequest = (CreateAccountRequest) target;
        if (accountRepository.existsByEmail(createAccountRequest.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{createAccountRequest.getEmail()},
                    "이미 사용중인 이메일입니다.");
        }
        if (accountRepository.existsByNickname(createAccountRequest.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{createAccountRequest.getEmail()},
                    "이미 사용중인 닉네임입니다.");
        }
    }
}
package jiny.futurevia.service.modules.account.endpoint.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class CreateAccountRequest {
    @NotBlank(message = "닉네임은 필수입니다")
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "닉네임은 3자 이상 20자까지 가능합니다")
    private String nickname;

    @Email(message = "이메일 형식 오류")
    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @NotBlank(message = "패스워드는 필수입니다")
    @Length(min = 8, max = 50)
    private String password;
}
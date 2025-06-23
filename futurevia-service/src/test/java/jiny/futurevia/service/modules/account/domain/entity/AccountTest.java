package jiny.futurevia.service.modules.account.domain.entity;

import jiny.futurevia.service.modules.account.exception.InvalidAccountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AccountTest {

    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_NICKNAME = "테스터";
    private static final String VALID_PASSWORD = "abcde123!";
    private static final Set<Role> VALID_ROLES = Set.of(new Role(1L, "ROLE_USER"));

    @DisplayName("Account를 정상적으로 생성한다.")
    @Test
    void Account를_정상적으로_생성한다() {
        // when & then
        assertDoesNotThrow(() -> Account.from(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, VALID_ROLES));
    }

    @DisplayName("이메일 형식이 올바르지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"abc@", "abc.com", "a@b", "abc@.com", "@abc.com"})
    void 이메일_형식이_올바르지_않으면_예외를_던진다(String invalidEmail) {
        // when & then
        assertThatThrownBy(() -> invokeValidate(invalidEmail, VALID_NICKNAME, VALID_PASSWORD, VALID_ROLES))
                .isInstanceOf(InvalidAccountException.class);
    }

    @DisplayName("닉네임이 비어있으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {""})
    void 닉네임_길이가_유효하지_않으면_예외를_던진다(String invalidNickname) {
        // when & then
        assertThatThrownBy(() -> invokeValidate(VALID_EMAIL, invalidNickname, VALID_PASSWORD, VALID_ROLES))
                .isInstanceOf(InvalidAccountException.class);
    }

    @DisplayName("닉네임이 100자를 초과하면 예외를 던진다.")
    @Test
    void 닉네임이_100자를_초과하면_예외를_던진다() {
        // given
        String longNickname = "a".repeat(101);

        // when & then
        assertThatThrownBy(() -> Account.from(VALID_EMAIL, longNickname, VALID_PASSWORD, VALID_ROLES))
                .isInstanceOf(InvalidAccountException.class);
    }

    @DisplayName("패스워드가 8자 미만이면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"1234567", "pass"})
    void 패스워드_8자미만_예외를_던진다(String shortPassword) {
        // when & then
        assertThatThrownBy(() -> invokeValidate(VALID_EMAIL, VALID_NICKNAME, shortPassword, VALID_ROLES))
                .isInstanceOf(InvalidAccountException.class);
    }

    @DisplayName("사용자 권한이 없으면 예외를 던진다.")
    @Test
    void 생성시_사용자_권한이_없으면_예외를_던진다() {
        // given
        Set<Role> noUserRole = Set.of(new Role(1L, "ROLE_ADMIN"));

        // when & then
        assertThatThrownBy(() -> invokeValidate(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, noUserRole))
                .isInstanceOf(InvalidAccountException.class);
    }

    private void invokeValidate(String email, String nickname, String password, Set<Role> roles) {
        Account.from(email, nickname, password, roles);
    }
}
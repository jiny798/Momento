package jiny.futurevia.service.modules.product.domain;

import static org.junit.jupiter.api.Assertions.*;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.domain.entity.Role;
import jiny.futurevia.service.modules.product.exception.InvalidProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_NICKNAME = "테스터";
    private static final String VALID_PASSWORD = "abcde123!";
    private static final Set<Role> VALID_ROLES = Set.of(new Role(1L, "ROLE_USER"));

    @Test
    @DisplayName("상품 이름이 null이거나 공백이면 예외를 던진다.")
    void 이름_없으면_예외() {
        // given
        String blankName = " ";
        Account account = Account.from(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, VALID_ROLES);

        // when & then
        assertThatThrownBy(() -> Product.create(
                blankName,
                "상세설명",
                1000L,
                List.of(),
                account,
                new Category(),
                1))
                .isInstanceOf(InvalidProductException.class)
                .hasMessageContaining("상품 이름은 필수입니다.");
    }

    @Test
    @DisplayName("상품 설명이 null이거나 공백이면 예외를 던진다.")
    void 설명_없으면_예외() {
        // given
        Account account = Account.from(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, VALID_ROLES);
        Category category = Category.create("TEST CATEGORY");

        assertThatThrownBy(() -> Product.create(
                "젤라또",
                null,
                1000L,
                List.of(),
                account,
                category,
                1)
        )
                .isInstanceOf(InvalidProductException.class)
                .hasMessageContaining("상품 설명은 필수입니다.");
    }

    @Test
    @DisplayName("상품 가격이 null이거나 0 이하이면 예외를 던진다.")
    void 가격_0_이하면_예외() {
        Account account = Account.from(VALID_EMAIL, VALID_NICKNAME, VALID_PASSWORD, VALID_ROLES);
        Category category = Category.create("TEST CATEGORY");

        assertThatThrownBy(() -> Product.create(
                "젤라또",
                "맛있어요",
                0L,
                List.of(),
                account,
                category,
                1)
        )
                .isInstanceOf(InvalidProductException.class)
                .hasMessageContaining("상품 가격은 0보다 커야 합니다.");
    }
}
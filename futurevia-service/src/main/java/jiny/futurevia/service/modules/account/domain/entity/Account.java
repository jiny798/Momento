package jiny.futurevia.service.modules.account.domain.entity;

import jakarta.persistence.*;

import jiny.futurevia.service.modules.account.exception.InvalidAccountException;
import jiny.futurevia.service.modules.common.AuditingEntity;
import jiny.futurevia.service.modules.order.domain.Address;
import jiny.futurevia.service.modules.order.domain.Order;
import jiny.futurevia.service.modules.product.domain.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account extends AuditingEntity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");
    private static final int MAX_NICKNAME_LENGTH = 100;
    private static final int MAX_PASSWORD_LENGTH = 100;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isValid;

    private String emailToken;

    private LocalDateTime emailTokenGeneratedAt;

    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<Product> products;

    @OneToMany(mappedBy = "account")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private Set<AccountRole> accountRoles = new HashSet<>();

    public static Account from(final String email,
                               final String nickname,
                               final String password,
                               final Set<Role> roles) {
        Account account = new Account();
        account.validateMember(email, nickname, password, roles);

        account.email = email;
        account.nickname = nickname;
        account.password = password;

        for (Role role : roles) {
            AccountRole accountRole = AccountRole.createOrderProduct(role);
            accountRole.setAccount(account);
            account.accountRoles.add(accountRole);
        }

        return account;
    }

    private void validateMember(final String email,
                                final String nickname,
                                final String password,
                                final Set<Role> roles) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidAccountException("이메일 형식이 올바르지 않습니다.");
        }

        if (nickname.isEmpty() || nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new InvalidAccountException(String.format("이름은 1자 이상 %d이하여야 합니다.", MAX_NICKNAME_LENGTH));
        }

        if (password.length() < 8) {
            throw new InvalidAccountException(String.format("패스워드는 8자 이상 이여야 합니다.", MAX_PASSWORD_LENGTH));
        }

        for (Role role : roles) {
            AccountRole accountRole = AccountRole.createOrderProduct(role);
            if (accountRole.getRole().getRoleName().equals("ROLE_USER")) {
                return;
            }
        }
        throw new InvalidAccountException("사용자 권한이 존재하지 않습니다");

    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void generateToken() {
        this.emailToken = UUID.randomUUID().toString();
        this.emailTokenGeneratedAt = LocalDateTime.now();
    }

    public boolean enableToSendEmail() {
        return this.emailTokenGeneratedAt.isBefore(LocalDateTime.now().minusMinutes(5));
    }

    public void verified() {
        this.isValid = true;
    }

    public boolean isValidEmailToken(String token) {
        return this.emailToken.equals(token);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account{");
        sb.append("id=").append(id);
        sb.append(", username='").append(nickname).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
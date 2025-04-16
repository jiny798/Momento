package jiny.futurevia.service.modules.account.domain.dto;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Data
public class AccountContext implements UserDetails {
    private Account account;
    private final List<GrantedAuthority> roles;

    public AccountContext(Account account, List<GrantedAuthority> roles) {
      this.account = account;
      this.roles = roles;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
    @Override
    public String getPassword() {
        return account.getPassword();
    }
    @Override
    public String getUsername() {
        return account.getNickname();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
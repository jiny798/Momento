package jiny.futurevia.service.modules.account.endpoint.dto;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String nickname;
    private final String email;

    public UserResponse(Account account) {
        this.id = account.getId();
        this.nickname = account.getNickname();
        this.email = account.getEmail();
    }
}
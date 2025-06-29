package jiny.futurevia.service.modules.account.support;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.domain.entity.Role;
import jiny.futurevia.service.modules.account.endpoint.dto.request.CreateAccountRequest;
import jiny.futurevia.service.modules.account.endpoint.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    UserResponse toDto(Account account);

    default Account toAccount(CreateAccountRequest request, Role role) {
        return Account.from(
                request.getEmail(),
                request.getNickname(),
                request.getPassword(),
                role);
    }
}
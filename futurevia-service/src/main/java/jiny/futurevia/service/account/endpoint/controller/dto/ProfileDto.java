package jiny.futurevia.service.account.endpoint.controller.dto;

import jiny.futurevia.service.account.domain.entity.Account;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileDto {
    @Length(max = 35)
    private String bio;
    @Length(max = 50)
    private String url;
    @Length(max = 50)
    private String job;
    @Length(max = 50)
    private String location;
    private String image;

    public static ProfileDto from(Account account) {
        return new ProfileDto(account);
    }

    protected ProfileDto(Account account) {
        this.bio = Optional.ofNullable(account.getProfile()).map(Account.Profile::getBio).orElse(null);
        this.url = Optional.ofNullable(account.getProfile()).map(Account.Profile::getUrl).orElse(null);
        this.job = Optional.ofNullable(account.getProfile()).map(Account.Profile::getJob).orElse(null);
        this.location = Optional.ofNullable(account.getProfile()).map(Account.Profile::getLocation).orElse(null);
        this.image = Optional.ofNullable(account.getProfile()).map(Account.Profile::getImage).orElse(null);
    }
}
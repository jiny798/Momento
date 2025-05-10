package jiny.futurevia.service.modules.account.domain.entity;

import jakarta.persistence.*;

import jiny.futurevia.service.modules.account.endpoint.dto.NotificationForm;
import jiny.futurevia.service.modules.account.endpoint.dto.ProfileDto;
import jiny.futurevia.service.modules.order.domain.Address;
import jiny.futurevia.service.modules.order.domain.Order;
import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.study.domain.entity.Study;
import jiny.futurevia.service.modules.tag.domain.entity.Tag;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Account extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")

    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;
    private LocalDateTime joinedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<Product> posts;

    @ToString.Exclude
    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ToString.Exclude
    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private boolean isValid;
    private String emailToken;
    private LocalDateTime emailTokenGeneratedAt;

    @Embedded
    private Profile profile = new Profile();;

    @Embedded
    private NotificationSetting notificationSetting = new NotificationSetting();

    @ToString.Exclude
    @JoinTable(name = "account_roles", joinColumns = {@JoinColumn(name = "account_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private Set<Role> userRoles = new HashSet<>();

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "account")
    private List<Order> orders = new ArrayList<>();

    public static Account from(String email, String nickname, String password, Set<Role> roles) {
        Account account = new Account();
        account.email = email;
        account.nickname = nickname;
        account.password = password;
        account.userRoles = roles;

        if (account.profile == null) {
            account.profile = new Profile();
        }
        if (account.notificationSetting == null) {
            account.notificationSetting = new NotificationSetting();
        }

        return account;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateProfile(ProfileDto profile) {
        if (this.profile == null) {
            this.profile = new Profile();
        }
        this.profile.bio = profile.getBio();
        this.profile.url = profile.getUrl();
        this.profile.job = profile.getJob();
        this.profile.location = profile.getLocation();
        this.profile.image = profile.getImage();
    }

    public void updateNotification(NotificationForm notificationForm) {
        this.notificationSetting.studyCreatedByEmail = notificationForm.isStudyCreatedByEmail();
        this.notificationSetting.studyCreatedByWeb = notificationForm.isStudyCreatedByWeb();
        this.notificationSetting.studyUpdatedByWeb = notificationForm.isStudyUpdatedByWeb();
        this.notificationSetting.studyUpdatedByEmail = notificationForm.isStudyUpdatedByEmail();
        this.notificationSetting.studyRegistrationResultByEmail = notificationForm.isStudyRegistrationResultByEmail();
        this.notificationSetting.studyRegistrationResultByWeb = notificationForm.isStudyRegistrationResultByWeb();
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
        joinedAt = LocalDateTime.now();
    }

    public boolean isValid(String token) {
        return this.emailToken.equals(token);
    }


    public boolean isManagerOf(Study study) {
        return study.getManagers().contains(this);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
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


    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @Getter
    @ToString
    public static class Profile {
        private String bio;
        private String url;
        private String job;
        private String location;
        private String company;

        @Lob
        @Basic(fetch = FetchType.EAGER)
        @Column(length = 500000)
        private String image;

    }

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    @Getter
    @ToString
    public static class NotificationSetting {
        private boolean studyCreatedByEmail = false;
        private boolean studyCreatedByWeb = true;
        private boolean studyRegistrationResultByEmail = false;
        private boolean studyRegistrationResultByWeb = true;
        private boolean studyUpdatedByEmail = false;
        private boolean studyUpdatedByWeb = true;
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
package jiny.futurevia.service.study.domain.entity;

import jakarta.persistence.*;
import jiny.futurevia.service.account.domain.dto.AccountContext;
import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.account.domain.entity.Zone;
import jiny.futurevia.service.study.form.StudyForm;
import jiny.futurevia.service.tag.domain.entity.Tag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();


    @ManyToMany
    private Set<Account> members = new HashSet<>();


    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();


    @ManyToMany
    private Set<Zone> zones = new HashSet<>();


    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    public static Study from(StudyForm studyForm) {
        Study study = new Study();
        study.title = studyForm.getTitle();
        study.shortDescription = studyForm.getShortDescription();
        study.fullDescription = studyForm.getFullDescription();
        study.path = studyForm.getPath();
        return study;
    }

    public void addManager(Account account) {
        managers.add(account);
    }

    public boolean isJoinable(AccountContext userAccount) {
        Account account = userAccount.getAccount();
        return this.isPublished() && this.isRecruiting() && !this.members.contains(account) && !this.managers.contains(account);
    }

    public boolean isMember(AccountContext userAccount) {
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(AccountContext userAccount) {
        return this.managers.contains(userAccount.getAccount());
    }
}
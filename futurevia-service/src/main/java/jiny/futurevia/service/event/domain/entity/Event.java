package jiny.futurevia.service.event.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jiny.futurevia.service.account.domain.dto.AccountContext;
import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.event.form.EventForm;
import jiny.futurevia.service.study.domain.entity.Study;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NamedEntityGraph(
    name = "Event.withEnrollments",
    attributeNodes = @NamedAttributeNode("enrollments")
)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Study study;

    @ManyToOne
    private Account createdBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private Integer limitOfEnrollments;

    @OneToMany(mappedBy = "event") @ToString.Exclude
    private List<Enrollment> enrollments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    public static Event from(EventForm eventForm, Account account, Study study) {
        Event event = new Event();
        event.eventType = eventForm.getEventType();
        event.description = eventForm.getDescription();
        event.endDateTime = eventForm.getEndDateTime();
        event.endEnrollmentDateTime = eventForm.getEndEnrollmentDateTime();
        event.limitOfEnrollments = eventForm.getLimitOfEnrollments();
        event.startDateTime = eventForm.getStartDateTime();
        event.title = eventForm.getTitle();
        event.createdBy = account;
        event.study = study;
        event.createdDateTime = LocalDateTime.now();
        return event;
    }

    public boolean isEnrollableFor(AccountContext userAccount) {
        return isNotClosed() && !isAlreadyEnrolled(userAccount);
    }

    public boolean isDisenrollableFor(AccountContext userAccount) {
        return isNotClosed() && isAlreadyEnrolled(userAccount);
    }

    private boolean isNotClosed() {
        return this.endEnrollmentDateTime.isAfter(LocalDateTime.now());
    }

    private boolean isAlreadyEnrolled(AccountContext userAccount) {
        Account account = userAccount.getAccount();
        for (Enrollment enrollment : this.enrollments) {
            if (enrollment.getAccount().equals(account)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAttended(AccountContext userAccount) {
        Account account = userAccount.getAccount();
        for (Enrollment enrollment : this.enrollments) {
            if (enrollment.getAccount().equals(account) && enrollment.isAttended()) {
                return true;
            }
        }
        return false;
    }

    public int numberOfRemainSpots() {
        int accepted = (int) this.enrollments.stream()
            .filter(Enrollment::isAccepted)
            .count();
        return this.limitOfEnrollments - accepted;
    }

    public Long getNumberOfAcceptedEnrollments() {
        return this.enrollments.stream()
            .filter(Enrollment::isAccepted)
            .count();
    }

    public void updateFrom(EventForm eventForm) {
        this.title = eventForm.getTitle();
        this.description = eventForm.getDescription();
        this.eventType = eventForm.getEventType();
        this.startDateTime = eventForm.getStartDateTime();
        this.endDateTime = eventForm.getEndDateTime();
        this.limitOfEnrollments = eventForm.getLimitOfEnrollments();
        this.endEnrollmentDateTime = eventForm.getEndEnrollmentDateTime();
    }

    public boolean isAbleToAcceptWaitingEnrollment() {
        // 선착순이고, 인원 여유가 있는지 체크
        return this.eventType == EventType.FCFS && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments();
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.attach(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.detachEvent();
    }

    public void acceptNextIfAvailable() {
        if (this.isAbleToAcceptWaitingEnrollment()) {
            this.firstWaitingEnrollment().ifPresent(Enrollment::accept);
        }
    }

    private Optional<Enrollment> firstWaitingEnrollment() {
        return this.enrollments.stream()
            .filter(e -> !e.isAccepted())
            .findFirst();
    }

    public void acceptWaitingList() {
        if (this.isAbleToAcceptWaitingEnrollment()) {
            List<Enrollment> waitingList = this.enrollments.stream()
                .filter(e -> !e.isAccepted())
                .collect(Collectors.toList());

            int numberToAccept = (int) Math.min(limitOfEnrollments - getNumberOfAcceptedEnrollments(), waitingList.size());

            waitingList.subList(0, numberToAccept).forEach(Enrollment::accept);
        }
    }

    public void accept(Enrollment enrollment) {
        if (this.eventType == EventType.CONFIRMATIVE && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments()) {
            enrollment.accept();
        }
    }

    // public void reject(Enrollment enrollment) {
    //     if (this.eventType == EventType.CONFIRMATIVE) {
    //         enrollment.reject();
    //     }
    // }

    public boolean isAcceptable(Enrollment enrollment) {
        return this.eventType == EventType.CONFIRMATIVE
            && this.enrollments.contains(enrollment)
            && this.limitOfEnrollments > this.getNumberOfAcceptedEnrollments()
            && !enrollment.isAttended()
            && !enrollment.isAccepted();
    }

    public boolean isRejectable(Enrollment enrollment) {
        return this.eventType == EventType.CONFIRMATIVE
            && this.enrollments.contains(enrollment)
            && !enrollment.isAttended()
            && enrollment.isAccepted();
    }
}
package jiny.futurevia.service.modules.event.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NamedEntityGraph(
        name = "Enrollment.withEventAndStudy",
        attributeNodes = {
                @NamedAttributeNode(value = "event", subgraph = "study")
        },
        subgraphs = @NamedSubgraph(name = "study", attributeNodes = @NamedAttributeNode("study"))
)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Account account;

    private LocalDateTime enrolledAt;

    private boolean accepted;

    private boolean attended;

    public static Enrollment of(LocalDateTime enrolledAt, boolean isAbleToAcceptWaitingEnrollment, Account account) {
        Enrollment enrollment = new Enrollment();
        enrollment.enrolledAt = enrolledAt;
        enrollment.accepted = isAbleToAcceptWaitingEnrollment;
        enrollment.account = account;
        return enrollment;
    }

    public void accept() {
        this.accepted = true;
    }

    public void attach(Event event) {
        this.event = event;
    }

    public void detachEvent() {
        this.event = null;
    }
    public void reject() {
        this.accepted = false;
    }

    public void attend() {
        this.attended = true;
    }

    public void absent() {
        this.attended = false;
    }

}
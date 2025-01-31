package jiny.futurevia.service.event.application;

import java.time.LocalDateTime;

import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.event.domain.entity.Enrollment;
import jiny.futurevia.service.event.domain.entity.Event;
import jiny.futurevia.service.event.form.EventForm;
import jiny.futurevia.service.event.infra.repository.EnrollmentRepository;
import jiny.futurevia.service.event.infra.repository.EventRepository;
import jiny.futurevia.service.study.domain.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final EnrollmentRepository enrollmentRepository;

    public Event createEvent(Study study, EventForm eventForm, Account account) {
        Event event = Event.from(eventForm, account, study);
        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        event.updateFrom(eventForm);
        event.acceptWaitingList();
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public void enroll(Event event, Account account) {
        if (!enrollmentRepository.existsByEventAndAccount(event, account)) {
            Enrollment enrollment = Enrollment.of(LocalDateTime.now(), event.isAbleToAcceptWaitingEnrollment(), account); // (3)
            event.addEnrollment(enrollment);
            enrollmentRepository.save(enrollment);
        }
    }

    public void leave(Event event, Account account) {
        Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);
        event.removeEnrollment(enrollment);
        enrollmentRepository.delete(enrollment);
        event.acceptNextIfAvailable();
    }
}
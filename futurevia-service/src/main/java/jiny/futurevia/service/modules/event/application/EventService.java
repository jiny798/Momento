package jiny.futurevia.service.modules.event.application;

import java.time.LocalDateTime;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.event.domain.entity.Enrollment;
import jiny.futurevia.service.modules.event.domain.entity.Event;
import jiny.futurevia.service.modules.event.event.EnrollmentAcceptedEvent;
import jiny.futurevia.service.modules.event.event.EnrollmentRejectedEvent;
import jiny.futurevia.service.modules.event.form.EventForm;
import jiny.futurevia.service.modules.event.infra.repository.EnrollmentRepository;
import jiny.futurevia.service.modules.event.infra.repository.EventRepository;
import jiny.futurevia.service.modules.study.domain.entity.Study;
import jiny.futurevia.service.modules.study.event.StudyUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final EnrollmentRepository enrollmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Event createEvent(Study study, EventForm eventForm, Account account) {
        Event event = Event.from(eventForm, account, study);
        eventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy(), "'" + event.getTitle() + "' 모임이 생성되었습니다."));
        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        event.updateFrom(eventForm);
        event.acceptWaitingList();
        eventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy(), "'" + event.getTitle() + "' 모임 정보가 수정되었습니다."));
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
        eventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy(), "'" + event.getTitle() + "' 모임이 취소되었습니다."));
    }

    // 사용자 - 참가 신청
    public void enroll(Event event, Account account) {
        if (!enrollmentRepository.existsByEventAndAccount(event, account)) {
            Enrollment enrollment = Enrollment.of(LocalDateTime.now(), event.isAbleToAcceptWaitingEnrollment(), account); // (3)
            event.addEnrollment(enrollment);
            enrollmentRepository.save(enrollment);
        }
    }

    // 사용자 - 참가 취소
    public void leave(Event event, Account account) {
        Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);
        if (!enrollment.isAttended()) {
            event.removeEnrollment(enrollment);
            enrollmentRepository.delete(enrollment);
            event.acceptNextIfAvailable();
        }
    }

    // 관리자 - 참가신청 승인
    public void acceptEnrollment(Event event, Enrollment enrollment) {
        event.accept(enrollment);
        eventPublisher.publishEvent(new EnrollmentAcceptedEvent(enrollment));
    }

    // 관리자 - 참가신청 거부
    public void rejectEnrollment(Event event, Enrollment enrollment) {
        event.reject(enrollment);
        eventPublisher.publishEvent(new EnrollmentRejectedEvent(enrollment));
    }

    // 출석 체크 
    public void checkInEnrollment(Event event, Enrollment enrollment) {
        enrollment.attend();
    }

    // 출석 취소
    public void cancelCheckinEnrollment(Event event, Enrollment enrollment) {
        enrollment.absent();
    }
}
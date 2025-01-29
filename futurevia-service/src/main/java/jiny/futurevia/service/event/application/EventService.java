package jiny.futurevia.service.event.application;

import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.event.domain.entity.Event;
import jiny.futurevia.service.event.form.EventForm;
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

    public Event createEvent(Study study, EventForm eventForm, Account account) {
        Event event = Event.from(eventForm, account, study);
        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        event.updateFrom(eventForm);
    }
}
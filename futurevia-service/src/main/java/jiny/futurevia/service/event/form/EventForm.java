package jiny.futurevia.service.event.form;


import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jiny.futurevia.service.event.domain.entity.Event;
import jiny.futurevia.service.event.domain.entity.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventForm {
    @NotBlank
    @Length(max = 50)
    private String title;

    private String description;

    private EventType eventType = EventType.FCFS;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endEnrollmentDateTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDateTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDateTime;

    @Min(2)
    private Integer limitOfEnrollments = 2;

    public static EventForm from(Event event) {
        EventForm eventForm = new EventForm();
        eventForm.title = event.getTitle();
        eventForm.description = event.getDescription();
        eventForm.eventType = event.getEventType();
        eventForm.endEnrollmentDateTime = event.getEndEnrollmentDateTime();
        eventForm.startDateTime = event.getStartDateTime();
        eventForm.endDateTime = event.getEndDateTime();
        eventForm.limitOfEnrollments = event.getLimitOfEnrollments();
        return eventForm;
    }

}
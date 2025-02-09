package jiny.futurevia.service.modules.event.event;

import jiny.futurevia.service.modules.event.domain.entity.Enrollment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EnrollmentEvent {
    protected final Enrollment enrollment;
    protected final String message;
}
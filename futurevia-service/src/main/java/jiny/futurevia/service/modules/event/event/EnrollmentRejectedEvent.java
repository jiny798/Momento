package jiny.futurevia.service.modules.event.event;

import jiny.futurevia.service.modules.event.domain.entity.Enrollment;

public class EnrollmentRejectedEvent extends EnrollmentEvent{
    public EnrollmentRejectedEvent(Enrollment enrollment) {
        super(enrollment, "모임 참가 신청이 거절되었습니다.");
    }
}
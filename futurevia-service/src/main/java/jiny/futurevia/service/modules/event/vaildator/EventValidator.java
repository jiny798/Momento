package jiny.futurevia.service.modules.event.vaildator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

import jiny.futurevia.service.modules.event.domain.entity.Event;
import jiny.futurevia.service.modules.event.form.EventForm;

@Component
public class EventValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return EventForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		EventForm eventForm = (EventForm)target;
		if (isEarlierThanNow(eventForm.getEndEnrollmentDateTime())) {
			errors.rejectValue("endEnrollmentDateTime", "wrong.datetime", "등록 마감 시간은 현재 시간보다 이전일 수 없습니다.");
		}
		if (isEarlierThanNow(eventForm.getStartDateTime())) {
			errors.rejectValue("startDateTime", "wrong.datetime", "모임 시작 날짜는 현재 시간보다 이전일 수 없습니다.");
		}
		if (isEarlierThanNow(eventForm.getEndDateTime())) {
			errors.rejectValue("endDateTime", "wrong.datetime", "모임 종료 날짜는 현재 시간보다 이전일 수 없습니다.");
		}

		if (isEarlierThan(eventForm.getEndDateTime(), eventForm.getStartDateTime()) ){
            errors.rejectValue("endDateTime", "wrong.datetime", "모임 종료 날짜는 모임 시작 날짜보다 이전일 수 없습니다.");
		}
		if (isEarlierThan(eventForm.getEndDateTime(), eventForm.getEndEnrollmentDateTime())) {
			errors.rejectValue("endDateTime", "wrong.datetime", "모임 종료 날짜는 등록 마감 시간보다 이전일 수 없습니다.");
		}

	}

	private boolean isEarlierThanNow(LocalDateTime time) {
		return time.isBefore(LocalDateTime.now());
	}

	private boolean isEarlierThan(LocalDateTime time, LocalDateTime targetTime) {
		return time.isBefore(targetTime);
	}

	public void validateUpdateForm(EventForm eventForm, Event event, Errors errors) {
		if (eventForm.getLimitOfEnrollments() < event.getNumberOfAcceptedEnrollments()) {
			errors.rejectValue("limitOfEnrollments", "wrong.value", "현재 신청된 참가 인원보다 모집 인원 수가 커야 합니다.");
		}
	}
}
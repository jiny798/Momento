package jiny.futurevia.service.modules.event.endpoint;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Optional;

import jiny.futurevia.service.infra.IntegrationTest;
import jiny.futurevia.service.modules.event.domain.entity.Enrollment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import jiny.futurevia.service.WithAccount;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.event.application.EventService;
import jiny.futurevia.service.modules.event.domain.entity.Event;
import jiny.futurevia.service.modules.event.domain.entity.EventType;
import jiny.futurevia.service.modules.event.form.EventForm;
import jiny.futurevia.service.modules.event.infra.repository.EnrollmentRepository;
import jiny.futurevia.service.modules.event.infra.repository.EventRepository;
import jiny.futurevia.service.modules.study.application.StudyService;
import jiny.futurevia.service.modules.study.domain.entity.Study;
import jiny.futurevia.service.modules.study.form.StudyForm;
import jiny.futurevia.service.modules.study.infra.repository.StudyRepository;

@IntegrationTest
class EventControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	StudyService studyService;
	@Autowired
	EventService eventService;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	StudyRepository studyRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	EnrollmentRepository enrollmentRepository;
	@Autowired
	ObjectMapper objectMapper;
	private final String studyPath = "study-path";
	private Study study;

	@BeforeEach
	void beforeEach() {
		Account account = accountRepository.findByNickname("jiny798");
		this.study = studyService.createNewStudy(StudyForm.builder()
			.path(studyPath)
			.shortDescription("description")
			.fullDescription("description")
			.title("title")
			.build(), account);
	}

	@AfterEach
	void afterEach() {
		studyRepository.deleteAll();
	}

	@Test
	@DisplayName("모임 만들기 폼")
	@WithAccount("jiny798")
	void eventForm() throws Exception {
		mockMvc.perform(get("/study/" + studyPath + "/new-event"))
			.andExpect(status().isOk())
			.andExpect(view().name("event/form"))
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("study"))
			.andExpect(model().attributeExists("eventForm"));
	}

	@Test
	@DisplayName("모임 생성 성공")
	@WithAccount("jiny798")
	void createEvent() throws Exception {
		LocalDateTime now = LocalDateTime.now();
		ResultActions resultActions = mockMvc.perform(post("/study/" + studyPath + "/new-event")
				.param("description", "description")
				.param("eventType", EventType.FCFS.name())
				.param("endDateTime", now.plusWeeks(3).toString())
				.param("endEnrollmentDateTime", now.plusWeeks(1).toString())
				.param("limitOfEnrollments", "2")
				.param("startDateTime", now.plusWeeks(2).toString())
				.param("title", "title")
				.with(csrf()));

		Event event = eventRepository.findAll()
				.stream()
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("등록된 모임이 없습니다."));

		resultActions.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/study/" + studyPath + "/events/" + event.getId()));
	}

	@Test
	@DisplayName("모임 생성 실패")
	@WithAccount("jiny798")
	void createEventWithErrors() throws Exception {
		LocalDateTime now = LocalDateTime.now();
		mockMvc.perform(post("/study/" + studyPath + "/new-event")
						.param("description", "description")
						.param("eventType", EventType.FCFS.name())
						.param("endDateTime", now.plusWeeks(3).toString())
						.param("endEnrollmentDateTime", now.plusWeeks(1).toString())
						.param("limitOfEnrollments", "2")
						.param("startDateTime", now.plusWeeks(2).toString())
						.param("title", "")
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("event/form"))
				.andExpect(model().attributeExists("account"))
				.andExpect(model().attributeExists("study"));
	}
	/// @@@@ 여기까지 씀

	@Test
	@DisplayName("모임 뷰 : 상세 보기")
	@WithAccount("jiny798")
	void eventView() throws Exception {
		Event event = stubbingEvent(EventType.FCFS);
		// 템플릿 내부 eventType 수정필요
		mockMvc.perform(get("/study/" + studyPath + "/events/" + event.getId()))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("study"))
			.andExpect(view().name("event/view"));
	}

	@Test
	@DisplayName("모임 리스트")
	@WithAccount("jiny798")
	void eventListView() throws Exception {
		stubbingEvent(EventType.FCFS);
		mockMvc.perform(get("/study/" + studyPath + "/events"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("study"))
			.andExpect(model().attributeExists("newEvents"))
			.andExpect(model().attributeExists("oldEvents"))
			.andExpect(view().name("study/events"));
	}

	@Test
	@DisplayName("모임 수정 뷰")
	@WithAccount("jiny798")
	void eventEditView() throws Exception {
		Event event = stubbingEvent(EventType.FCFS);
		mockMvc.perform(get("/study/" + studyPath + "/events/" + event.getId() + "/edit"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("account"))
			.andExpect(model().attributeExists("study"))
			.andExpect(model().attributeExists("event"))
			.andExpect(model().attributeExists("eventForm"))
			.andExpect(view().name("event/update-form"));
	}

//	@Test
//	@DisplayName("모임 수정")
//	@WithAccount("jiny798")
//	void editEvent() throws Exception {
//		Event event = stubbingEvent();
//		EventForm eventForm = EventForm.from(event);
//		eventForm.setTitle("another");
//		mockMvc.perform(post("/study/" + studyPath + "/events/" + event.getId() + "/edit")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsBytes(eventForm))
//				.with(csrf()))
//			.andExpect(status().is3xxRedirection())
//			.andExpect(redirectedUrl("/study/" + studyPath + "/events/" + event.getId()));
//	}
@Test
@DisplayName("모임 수정")
@WithAccount("jiny798")
void editEvent() throws Exception {
	Event event = stubbingEvent(EventType.FCFS);
	LocalDateTime now = LocalDateTime.now();
	mockMvc.perform(post("/study/" + studyPath + "/events/" + event.getId() + "/edit")
					.param("description", "description")
					.param("eventType", EventType.FCFS.name())
					.param("endDateTime", now.plusWeeks(3).toString())
					.param("endEnrollmentDateTime", now.plusWeeks(1).toString())
					.param("limitOfEnrollments", "2")
					.param("startDateTime", now.plusWeeks(2).toString())
					.param("title", "anotherTitle")
					.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/study/" + studyPath + "/events/" + event.getId()));
}

	@Test
	@DisplayName("모임 삭제")
	@WithAccount("jiny798")
	void deleteEvent() throws Exception {
		Event event = stubbingEvent(EventType.FCFS);
		mockMvc.perform(delete("/study/" + studyPath + "/events/" + event.getId())
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/study/" + studyPath + "/events"));
		Optional<Event> byId = eventRepository.findById(event.getId());
		assertEquals(Optional.empty(), byId);
	}

	@Test
	@DisplayName("선착순 모임에 참가 신청 : 자동 수락")
	@WithAccount("jiny798")
	void enroll() throws Exception {
		Event event = stubbingEvent(EventType.FCFS);
		mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));
		Account account = accountRepository.findByNickname("jiny798");
		isAccepted(account, event);
	}

	@Test
	@DisplayName("선착순 모임에 참가 신청 : 대기중")
	@WithAccount("jiny798")
	void enroll_with_waiting() throws Exception {
		Event event = stubbingEvent(EventType.FCFS);
		Account tester1 = createAccount("tester1");
		Account tester2 = createAccount("tester2");
		eventService.enroll(event, tester1);
		eventService.enroll(event, tester2);
		mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/enroll")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));
		Account jiny = accountRepository.findByNickname("jiny798");
		isNotAccepted(jiny, event);
	}

	@Test
	@DisplayName("참가신청 확정자가 취소하는 경우: 다음 대기자 자동 신청")
	@WithAccount("jiny798")
	void leave_auto_enroll() throws Exception {
		Account JINY = accountRepository.findByNickname("jiny798");
		Account tester1 = createAccount("tester1");
		Account tester2 = createAccount("tester2");
		Event event = stubbingEvent(EventType.FCFS);
		eventService.enroll(event, tester1);
		eventService.enroll(event, JINY);
		eventService.enroll(event, tester2);
		isAccepted(tester1, event);
		isAccepted(JINY, event);
		isNotAccepted(tester2, event);
		mockMvc.perform(post("/study/" + study.getPath() + "/events/" + event.getId() + "/leave")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/study/" + study.getPath() + "/events/" + event.getId()));
		isAccepted(tester1, event);
		isAccepted(tester2, event);
		assertNull(enrollmentRepository.findByEventAndAccount(event, JINY));

	}

	@Test
	@DisplayName("참가 신청 수락")
	@WithAccount("jiny798")
	void accept() throws Exception {
		Account manager = accountRepository.findByNickname("jiny798");
		Account account = createAccount("member");
		Event event = stubbingEvent(EventType.CONFIRMATIVE, manager);
		eventService.enroll(event, account);
		Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);

		mockMvc.perform(get("/study/" + study.getPath() + "/events/" + event.getId() + "/enrollments/" + enrollment.getId() + "/accept"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

		assertTrue(enrollment.isAccepted());
	}

	@Test
	@DisplayName("참가 신청 거절")
	@WithAccount("jiny798")
	void reject() throws Exception {
		Account manager = accountRepository.findByNickname("jiny798");
		Account account = createAccount("member");
		Event event = stubbingEvent(EventType.CONFIRMATIVE, manager);
		eventService.enroll(event, account);
		Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);

		mockMvc.perform(get("/study/" + study.getPath() + "/events/" + event.getId() + "/enrollments/" + enrollment.getId() + "/reject"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

		assertFalse(enrollment.isAccepted());
	}

	@Test
	@DisplayName("출석 체크")
	@WithAccount("jiny798")
	void checkin() throws Exception {
		Account manager = accountRepository.findByNickname("jiny798");
		Account account = createAccount("member");
		Event event = stubbingEvent(EventType.CONFIRMATIVE, manager);
		eventService.enroll(event, account);
		Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);
		eventService.acceptEnrollment(event, enrollment);

		mockMvc.perform(get("/study/" + study.getPath() + "/events/" + event.getId() + "/enrollments/" + enrollment.getId() + "/checkin"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

		assertTrue(enrollment.isAttended());
	}

	@Test
	@DisplayName("출석 체크 취소")
	@WithAccount("jiny798")
	void cancelCheckin() throws Exception {
		Account manager = accountRepository.findByNickname("jiny798");
		Account account = createAccount("member");
		Event event = stubbingEvent(EventType.CONFIRMATIVE, manager);
		eventService.enroll(event, account);
		Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);
		eventService.acceptEnrollment(event, enrollment);

		mockMvc.perform(get("/study/" + study.getPath() + "/events/" + event.getId() + "/enrollments/" + enrollment.getId() + "/cancel-checkin"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/study/" + study.getEncodedPath() + "/events/" + event.getId()));

		assertFalse(enrollment.isAttended());
	}

	private void isNotAccepted(Account account, Event event) {
		assertFalse(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
	}

	private void isAccepted(Account account, Event event) {
		assertTrue(enrollmentRepository.findByEventAndAccount(event, account).isAccepted());
	}

	private Event stubbingEvent(EventType eventType, Account... accounts) {
		Study study = studyRepository.findByPath(studyPath);
		Account account = null;
		if (accounts == null || accounts.length == 0) {
			account = createAccount("manager");
		} else {
			account = accounts[0];
		}
		LocalDateTime now = LocalDateTime.now();
		EventForm eventForm = EventForm.builder()
				.description("description")
				.eventType(eventType)
				.endDateTime(now.plusWeeks(3))
				.endEnrollmentDateTime(now.plusWeeks(1))
				.limitOfEnrollments(2)
				.startDateTime(now.plusWeeks(2))
				.title("title")
				.build();
		return eventService.createEvent(study, eventForm, account);
	}

	private Account createAccount(String nickname) {
		return accountRepository.save(Account.with(nickname + "@example.com", nickname, "password"));
	}
}
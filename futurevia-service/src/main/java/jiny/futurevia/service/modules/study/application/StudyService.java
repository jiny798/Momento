package jiny.futurevia.service.modules.study.application;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.domain.entity.Zone;
import jiny.futurevia.service.modules.study.domain.entity.Study;
import jiny.futurevia.service.modules.study.domain.entity.StudyCreatedEvent;
import jiny.futurevia.service.modules.study.event.StudyUpdateEvent;
import jiny.futurevia.service.modules.study.form.StudyDescriptionForm;
import jiny.futurevia.service.modules.study.form.StudyForm;
import jiny.futurevia.service.modules.study.infra.repository.StudyRepository;
import jiny.futurevia.service.modules.tag.domain.entity.Tag;
import jiny.futurevia.service.modules.tag.infra.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TagRepository tagRepository;

    public Study createNewStudy(StudyForm studyForm, Account account) {
        Study study = Study.from(studyForm);
        study.addManager(account);
        return studyRepository.save(study);
    }

    // 일반 사용자
    public Study getStudy(String path) {
        Study study = studyRepository.findByPath(path);
        checkStudyExists(path, study);
        return study;
    }

    // 관리자
    public Study getStudyToUpdate(Account account, String path) {
        return getStudy(account, path, studyRepository.findByPath(path));
    }
    public Study getStudyToUpdateTag(Account account, String path) {
        return getStudy(account, path, studyRepository.findStudyWithTagsByPath(path));
    }

    public Study getStudyToUpdateZone(Account account, String path) {
        return getStudy(account, path, studyRepository.findStudyWithZonesByPath(path));
    }

    public Study getStudyToUpdateStatus(Account account, String path) {
        return getStudy(account, path, studyRepository.findStudyWithManagersByPath(path));
    }

    private Study getStudy(Account account, String path, Study studyByPath) {
        checkStudyExists(path, studyByPath);
        checkAccountIsManager(account, studyByPath);
        return studyByPath;
    }

    public Study getStudyToEnroll(String path) {
        // 참가신청은 스터디 정보만 필요하므로, Study 만 조회
        return studyRepository.findStudyOnlyByPath(path)
            .orElseThrow(() -> new IllegalArgumentException(path + "에 해당하는 스터디가 존재하지 않습니다."));
    }

    private void checkStudyExists(String path, Study study) {
        if (study == null) {
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
        }
    }

    public void publish(Study study) {
        study.publish();
        eventPublisher.publishEvent(new StudyCreatedEvent(study));
    }

    public void close(Study study) {
        study.close();
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "스터디를 종료했습니다."));
    }

    public void startRecruit(Study study) {
        study.startRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "팀원 모집을 시작합니다."));
    }

    public void stopRecruit(Study study) {
        study.stopRecruit();
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "팀원 모집을 종료했습니다."));
    }

    public boolean isValidPath(String newPath) {
        if (!newPath.matches(StudyForm.VALID_PATH_PATTERN)) {
            return false;
        }
        return !studyRepository.existsByPath(newPath);
    }

    public void updateStudyPath(Study study, String newPath) {
        study.updatePath(newPath);
    }

    public boolean isValidTitle(String newTitle) {
        return newTitle.length() <= 50;
    }

    public void updateStudyTitle(Study study, String newTitle) {
        study.updateTitle(newTitle);
    }

    public void remove(Study study) {
        if (!study.isRemovable()) {
            throw new IllegalStateException("스터디를 삭제할 수 없습니다.");
        }
        studyRepository.delete(study);
    }

    private void checkAccountIsManager(Account account, Study study) {
        if (!account.isManagerOf(study)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
    }


    public void updateStudyDescription(Study study, StudyDescriptionForm studyDescriptionForm) { // (3)
        study.updateDescription(studyDescriptionForm);
        eventPublisher.publishEvent(new StudyUpdateEvent(study, "스터디 소개를 수정했습니다."));
    }

    public void updateStudyImage(Study study, String image) {
        study.updateImage(image);
    }

    public void enableStudyBanner(Study study) {
        study.setBanner(true);
    }

    public void disableStudyBanner(Study study) {
        study.setBanner(false);
    }


    public void addTag(Study study, Tag tag) {
        study.addTag(tag);
    }

    public void removeTag(Study study, Tag tag) {
        study.removeTag(tag);
    }

    public void addZone(Study study, Zone zone) {
        study.addZone(zone);
    }

    public void removeZone(Study study, Zone zone) {
        study.removeZone(zone);
    }

    public void addMember(Study study, Account account) {
        study.addMember(account);
    }

    public void removeMember(Study study, Account account) {
        study.removeMember(account);
    }

    public void generateTestStudies(Account account) {
        for (int i = 0; i < 30; i++) {
            String randomValue = RandomString.make(5);
            Study study = createNewStudy(StudyForm.builder()
                    .title("테스트 스터디 " + randomValue)
                    .path("test-" + randomValue)
                    .shortDescription("테스트용 스터디 입니다.")
                    .fullDescription("test")
                    .build(), account);
            study.publish();
            Tag jpa = tagRepository.findByTitle("jpa").orElse(null);
            study.getTags().add(jpa);
        }
    }
}
package jiny.futurevia.service.study.application;

import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.study.domain.entity.Study;
import jiny.futurevia.service.study.form.StudyForm;
import jiny.futurevia.service.study.infra.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;

    public Study createNewStudy(StudyForm studyForm, Account account) {
        Study study = Study.from(studyForm);
        study.addManager(account);
        return studyRepository.save(study);
    }
}
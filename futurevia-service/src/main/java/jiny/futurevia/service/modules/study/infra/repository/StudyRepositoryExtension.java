package jiny.futurevia.service.modules.study.infra.repository;

import jiny.futurevia.service.modules.study.domain.entity.Study;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {
    List<Study> findByKeyword(String keyword);
}
package jiny.futurevia.service.modules.study.infra.repository;

import jiny.futurevia.service.modules.account.domain.entity.Zone;
import jiny.futurevia.service.modules.study.domain.entity.Study;
import jiny.futurevia.service.modules.tag.domain.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {
    Page<Study> findByKeyword(String keyword, Pageable pageable);

    List<Study> findByAccount(Set<Tag> tags, Set<Zone> zones);
}
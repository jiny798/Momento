package jiny.futurevia.service.modules.study.infra.repository;

import com.querydsl.jpa.JPQLQuery;
import jiny.futurevia.service.modules.study.domain.entity.QStudy;
import jiny.futurevia.service.modules.study.domain.entity.Study;
import jiny.futurevia.service.modules.tag.domain.entity.QTag;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class StudyRepositoryExtensionImpl extends QuerydslRepositorySupport implements StudyRepositoryExtension {

    public StudyRepositoryExtensionImpl() {
        super(Study.class);
    }

    @Override
    public List<Study> findByKeyword(String keyword) {
        QStudy study = QStudy.study;
        JPQLQuery<Study> query = from(study)
                .where(study.published.isTrue()
                        .and(study.title.containsIgnoreCase(keyword))
                        .or(study.tags.any().title.containsIgnoreCase(keyword))
                        .or(study.zones.any().localNameOfCity.containsIgnoreCase(keyword)))
            .leftJoin(study.tags, QTag.tag);
        return query.fetch() ;
    }
}
package jiny.futurevia.service.modules.event.infra.repository;

import java.util.List;

import jiny.futurevia.service.modules.event.domain.entity.Event;
import jiny.futurevia.service.modules.study.domain.entity.Study;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {
	@EntityGraph(value = "Event.withEnrollments", type = EntityGraph.EntityGraphType.LOAD)
	List<Event> findByStudyOrderByStartDateTime(Study study);
}
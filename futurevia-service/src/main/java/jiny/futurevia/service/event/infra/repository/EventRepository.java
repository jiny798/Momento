package jiny.futurevia.service.event.infra.repository;

import java.util.List;

import jiny.futurevia.service.event.domain.entity.Event;
import jiny.futurevia.service.study.domain.entity.Study;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface EventRepository extends JpaRepository<Event, Long> {
	@EntityGraph(value = "Event.withEnrollments", type = EntityGraph.EntityGraphType.LOAD)
	List<Event> findByStudyOrderByStartDateTime(Study study);
}
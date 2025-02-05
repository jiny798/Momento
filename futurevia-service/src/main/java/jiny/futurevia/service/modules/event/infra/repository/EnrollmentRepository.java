package jiny.futurevia.service.modules.event.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.event.domain.entity.Enrollment;
import jiny.futurevia.service.modules.event.domain.entity.Event;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByEventAndAccount(Event event, Account account);

    Enrollment findByEventAndAccount(Event event, Account account);
}
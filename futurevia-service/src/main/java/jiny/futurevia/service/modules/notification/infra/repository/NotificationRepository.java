package jiny.futurevia.service.modules.notification.infra.repository;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countByAccountAndChecked(Account account, boolean checked);
}
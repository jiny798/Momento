package jiny.futurevia.service.modules.order.infra.repository;

import jiny.futurevia.service.modules.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccountIdAndOrderDateBetween(Long accountId, LocalDateTime start, LocalDateTime end);
}

package jiny.futurevia.service.modules.order.infra.repository;

import jiny.futurevia.service.modules.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

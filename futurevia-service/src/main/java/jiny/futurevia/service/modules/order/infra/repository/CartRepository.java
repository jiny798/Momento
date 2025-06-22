package jiny.futurevia.service.modules.order.infra.repository;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByAccount(Account account);
}

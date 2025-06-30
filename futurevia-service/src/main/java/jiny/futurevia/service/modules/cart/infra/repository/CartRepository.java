package jiny.futurevia.service.modules.cart.infra.repository;

import jiny.futurevia.service.modules.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {}
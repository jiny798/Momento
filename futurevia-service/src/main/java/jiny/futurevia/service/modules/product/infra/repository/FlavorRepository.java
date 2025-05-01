package jiny.futurevia.service.modules.product.infra.repository;


import jiny.futurevia.service.modules.product.domain.Flavor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlavorRepository extends JpaRepository<Flavor, Long> {
}

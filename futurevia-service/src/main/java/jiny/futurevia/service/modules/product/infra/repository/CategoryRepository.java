package jiny.futurevia.service.modules.product.infra.repository;

import jiny.futurevia.service.modules.product.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Category findByName(String name);
}

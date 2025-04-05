package jiny.futurevia.service.modules.post.infra.repository;

import jiny.futurevia.service.modules.post.domain.Product;
import jiny.futurevia.service.modules.post.infra.repository.querydsl.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositoryCustom {

}

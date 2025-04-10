package jiny.futurevia.service.modules.product.infra.repository;

import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.infra.repository.querydsl.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositoryCustom {

}

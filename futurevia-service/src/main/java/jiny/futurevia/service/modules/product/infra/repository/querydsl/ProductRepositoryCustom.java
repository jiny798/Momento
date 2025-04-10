package jiny.futurevia.service.modules.product.infra.repository.querydsl;

import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.endpoint.dto.request.ProductSearch;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {

    Page<Product> getList(ProductSearch productSearch);
}

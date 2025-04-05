package jiny.futurevia.service.modules.post.infra.repository.querydsl;

import jiny.futurevia.service.modules.post.domain.Product;
import jiny.futurevia.service.modules.post.endpoint.dto.request.ProductSearch;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {

    Page<Product> getList(ProductSearch productSearch);
}

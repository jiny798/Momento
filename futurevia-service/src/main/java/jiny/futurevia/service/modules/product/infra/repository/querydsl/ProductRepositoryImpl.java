package jiny.futurevia.service.modules.product.infra.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.endpoint.dto.request.ProductSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static jiny.futurevia.service.modules.product.domain.QProduct.product;


@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Product> getList(ProductSearch productSearch) {
        long totalCount = jpaQueryFactory.select(product.count())
                .from(product)
                .fetchFirst();

        List<Product> items = jpaQueryFactory.selectFrom(product)
                .limit(productSearch.getSize())
                .offset(productSearch.getOffset())
                .orderBy(product.id.desc())
                .fetch();

        return new PageImpl<>(items, productSearch.getPageable(), totalCount);
    }
}

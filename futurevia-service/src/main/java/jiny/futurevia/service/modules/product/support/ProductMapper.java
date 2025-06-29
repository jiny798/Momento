package jiny.futurevia.service.modules.product.support;

import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.endpoint.dto.request.RequestProduct;
import jiny.futurevia.service.modules.product.endpoint.dto.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toDto(Product product);

    Product toEntity(RequestProduct requestProduct);
}
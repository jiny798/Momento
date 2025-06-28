package jiny.futurevia.service.modules.product.support;

import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.endpoint.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toDto(Product product);
}
package jiny.futurevia.service.modules.cart.support;

import jiny.futurevia.service.modules.cart.domain.Cart;
import jiny.futurevia.service.modules.cart.endpoint.dto.response.CartResponse;
import jiny.futurevia.service.modules.product.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "productImageUrl", expression = "java(getFirstImageUrl(cart.getProduct()))")
    CartResponse toDto(Cart cart);

    List<CartResponse> toDtoList(List<Cart> carts);

    default String getFirstImageUrl(Product product) {
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            return product.getProductImages().get(0).getImageUrl();
        }
        return null;
    }
}
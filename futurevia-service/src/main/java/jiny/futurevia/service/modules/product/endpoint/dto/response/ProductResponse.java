package jiny.futurevia.service.modules.product.endpoint.dto.response;

import jiny.futurevia.service.modules.product.domain.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class  ProductResponse {

    private final String title;
    private final Long price;
    private final String shippingMethod;
    private final Long shippingFee;
    private final Long minOrderQuantity;
    private final Long stockQuantity;
    private final String details;
    private final Boolean isDefect;
    private final LocalDateTime createdAt;

    public ProductResponse(Product product) {
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.shippingMethod = product.getShippingMethod();
        this.shippingFee = product.getShippingFee();
        this.minOrderQuantity = product.getMinOrderQuantity();
        this.stockQuantity = product.getStockQuantity();
        this.details = product.getDetails();
        this.isDefect = product.getIsDefect();
        this.createdAt = product.getCreatedAt();

    }


}

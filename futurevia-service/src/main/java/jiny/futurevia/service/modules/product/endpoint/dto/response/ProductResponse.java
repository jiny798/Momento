package jiny.futurevia.service.modules.product.endpoint.dto.response;

import jiny.futurevia.service.modules.product.domain.Product;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class  ProductResponse {

    private final Long id;
    private final String title;
    private final Long price;
    private final Long stockQuantity;
    private final String details;
    private final List<String> images;
    private final LocalDateTime createdAt;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.details = product.getDetails();
        this.createdAt = product.getCreatedAt();
        this.images = product.getImageUrls();
    }


}

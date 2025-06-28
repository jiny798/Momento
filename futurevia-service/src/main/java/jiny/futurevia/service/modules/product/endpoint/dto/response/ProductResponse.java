package jiny.futurevia.service.modules.product.endpoint.dto.response;

import jiny.futurevia.service.modules.product.domain.Product;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class  ProductResponse {

    private final Long id;
    private final String name;
    private final Long price;
    private final int stock;
    private final String description;
    private final int flavorSelectCount;
    private final List<String> productImages;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.description = product.getDescription();
        this.flavorSelectCount = product.getFlavorSelectCount();
        this.productImages = new ArrayList<>();
    }

}

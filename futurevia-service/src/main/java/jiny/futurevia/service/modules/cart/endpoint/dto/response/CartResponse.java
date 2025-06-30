package jiny.futurevia.service.modules.cart.endpoint.dto.response;

import lombok.Data;

@Data
public class CartResponse {
    private String name;
    private Long price;
    private Integer quantity;
    private String options;
    private String productImageUrl;
}

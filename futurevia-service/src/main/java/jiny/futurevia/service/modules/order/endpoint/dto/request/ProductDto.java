package jiny.futurevia.service.modules.order.endpoint.dto.request;

import lombok.*;

@Data
public class ProductDto {
    private Long productId;
    private Integer count;
    private String option;
}

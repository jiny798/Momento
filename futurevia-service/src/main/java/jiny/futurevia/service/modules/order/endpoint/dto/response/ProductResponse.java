package jiny.futurevia.service.modules.order.endpoint.dto.response;

import lombok.*;

/*
 * 상품 목록에 보여줄 DTO
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private String image;
    private String productName;
    private Long price;
    private Integer count;
    private String option;
}
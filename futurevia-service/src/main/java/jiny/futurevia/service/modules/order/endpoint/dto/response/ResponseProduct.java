package jiny.futurevia.service.modules.order.endpoint.dto.response;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProduct {
    private Long productId;
    private String productName;
    private Long price;
    private Integer count;
    private String option;
}
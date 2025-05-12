package jiny.futurevia.service.modules.order.endpoint.dto.request;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestProduct {
    private Long productId;
    private Integer count;
    private List<String> flavors;
}

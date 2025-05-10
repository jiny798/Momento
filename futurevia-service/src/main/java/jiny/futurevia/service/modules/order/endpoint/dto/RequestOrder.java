package jiny.futurevia.service.modules.order.endpoint.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestOrder {
    List<RequestProduct> requestProductList;
}

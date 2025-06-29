package jiny.futurevia.service.modules.order.endpoint.dto.request;

import lombok.*;

import java.util.List;

@Data
public class OrderRequest {
    private Long orderId;
    private AddressDto addressDto;
    private List<ProductDto> productDtoList;
}

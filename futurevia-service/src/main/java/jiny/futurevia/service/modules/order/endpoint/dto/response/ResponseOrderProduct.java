package jiny.futurevia.service.modules.order.endpoint.dto.response;


import jiny.futurevia.service.modules.order.domain.DeliveryStatus;
import jiny.futurevia.service.modules.order.domain.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

/*
 * 구매 내역에 보여줄 주문 상품
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOrderProduct {
    private Long productId;
    private LocalDateTime purchaseDate;
    private OrderStatus status;
    private DeliveryStatus deliveryStatus;
    private int totalPrice;
}

package jiny.futurevia.service.modules.order.endpoint.dto.response;


import jiny.futurevia.service.modules.order.domain.DeliveryStatus;
import jiny.futurevia.service.modules.order.domain.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/*
 * 구매 내역에 보여줄 주문 상품
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductResponse {
    private List<String> image;
    private List<Integer> count;
    private List<Long> productId; // 주문 하나에 여러 상품 가능
    private List<String> productName; // 주문 하나에 여러 상품 가능
    private LocalDateTime purchaseDate;
    private OrderStatus status;
    private DeliveryStatus deliveryStatus;
    private int totalPrice;
}

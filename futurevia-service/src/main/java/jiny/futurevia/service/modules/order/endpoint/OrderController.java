package jiny.futurevia.service.modules.order.endpoint;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.common.response.ApiResponse;
import jiny.futurevia.service.modules.cart.application.CartService;
import jiny.futurevia.service.modules.order.application.OrderService;
import jiny.futurevia.service.modules.order.endpoint.dto.request.OrderDateDto;
import jiny.futurevia.service.modules.order.endpoint.dto.request.OrderRequest;
import jiny.futurevia.service.modules.order.endpoint.dto.request.ProductDto;
import jiny.futurevia.service.modules.order.endpoint.dto.response.ResponseOrderProduct;
import jiny.futurevia.service.modules.order.endpoint.dto.response.ResponseProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    @PostMapping("/order")
    public ApiResponse<Void> order(@AuthenticationPrincipal Account account, @RequestBody OrderRequest orderRequest) {
        log.debug("Order requested: {}", orderRequest);
        orderService.orderProduct(account.getId(), orderRequest);
        return ApiResponse.success();
    }

    @GetMapping("/order")
    public ApiResponse<List<ResponseOrderProduct>> showOrderList(@AuthenticationPrincipal Account account,
                                                                 @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                 @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ResponseOrderProduct> responseOrderProductList = orderService.getOrderProducts(account.getId(), new OrderDateDto(startDate, endDate));
        return ApiResponse.success(responseOrderProductList);
    }

    @PostMapping("/order/cancel")
    public ApiResponse<Void> cancel(@AuthenticationPrincipal Account account, @RequestBody OrderRequest orderRequest) {
        orderService.cancelOrder(orderRequest.getOrderId());
        return ApiResponse.success();
    }

}

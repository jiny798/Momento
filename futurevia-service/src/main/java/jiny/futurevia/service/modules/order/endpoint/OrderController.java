package jiny.futurevia.service.modules.order.endpoint;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.common.response.ApiResponse;
import jiny.futurevia.service.modules.order.application.CartService;
import jiny.futurevia.service.modules.order.application.OrderService;
import jiny.futurevia.service.modules.order.endpoint.dto.request.OrderDate;
import jiny.futurevia.service.modules.order.endpoint.dto.request.RequestOrder;
import jiny.futurevia.service.modules.order.endpoint.dto.request.RequestProduct;
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
    public ApiResponse<Void> order(@AuthenticationPrincipal Account account, @RequestBody RequestOrder requestOrder) {
        log.debug("Order requested: {}", requestOrder);
        orderService.order(account.getId(), requestOrder);
        return ApiResponse.success();
    }

    @GetMapping("/order")
    public ApiResponse<List<ResponseOrderProduct>> showOrderList(@AuthenticationPrincipal Account account,
                                                                 @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                 @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ResponseOrderProduct> responseOrderProductList = orderService.getOrderProducts(account.getId(), new OrderDate(startDate, endDate));
        return ApiResponse.success(responseOrderProductList);
    }

    @PostMapping("/order/cancel")
    public ApiResponse<Void> cancel(@AuthenticationPrincipal Account account, @RequestBody RequestOrder requestOrder) {
        orderService.cancelOrder(requestOrder.getOrderId());
        return ApiResponse.success();
    }

    @PostMapping("/cart")
    public ApiResponse<Void> addCart(@AuthenticationPrincipal Account account, @RequestBody RequestProduct requestProduct) {
        log.info("add cart {}", requestProduct);
        cartService.addCart(account.getId(), requestProduct);
        return ApiResponse.success();
    }

    @GetMapping("/cart")
    public ApiResponse<List<ResponseProduct>> showCartList(@AuthenticationPrincipal Account account) {
        List<ResponseProduct> responseProductList = cartService.getCartList(account.getId());
        return ApiResponse.success(responseProductList);
    }

}

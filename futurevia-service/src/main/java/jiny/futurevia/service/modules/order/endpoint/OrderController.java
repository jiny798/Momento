package jiny.futurevia.service.modules.order.endpoint;

import jiny.futurevia.service.modules.account.domain.entity.Account;
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
    public void order(@AuthenticationPrincipal Account account, @RequestBody RequestOrder requestOrder) {
        log.debug("Order requested: {}", requestOrder);
        orderService.order(account.getId(), requestOrder);
    }

    @GetMapping("/order")
    public List<ResponseOrderProduct> showOrderList(@AuthenticationPrincipal Account account,
                                                    @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        System.out.println("mmm");
        return orderService.getOrderProducts(account.getId(), new OrderDate(startDate, endDate));
    }

    @PostMapping("/order/cancel")
    public void cancel(@AuthenticationPrincipal Account account, @RequestBody RequestOrder requestOrder) {
        orderService.cancelOrder(requestOrder.getOrderId());
    }

    @PostMapping("/cart")
    public void addCart(@AuthenticationPrincipal Account account, @RequestBody RequestProduct requestProduct) {
        log.info("add cart {}", requestProduct);
        cartService.addCart(account.getId(), requestProduct);
    }

    @GetMapping("/cart")
    public List<ResponseProduct> showCartList(@AuthenticationPrincipal Account account) {
        return cartService.getCartList(account.getId());
    }


}

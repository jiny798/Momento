package jiny.futurevia.service.modules.order.endpoint;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.order.application.OrderService;
import jiny.futurevia.service.modules.order.endpoint.dto.RequestOrder;
import jiny.futurevia.service.modules.order.endpoint.dto.RequestProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public void order(@AuthenticationPrincipal Account account, @RequestBody RequestOrder requestOrder) {
        orderService.order(account.getId(), requestOrder);
    }

    @PostMapping("/cart")
    public void addCart(@AuthenticationPrincipal Account account, @RequestBody RequestProduct requestProduct) {
        orderService.addCart(account.getId(), requestProduct);
    }

    @GetMapping("/cart")
    public void showCartList(@AuthenticationPrincipal Account account, @RequestBody RequestProduct requestProduct) {
        // CART 상태인것만 보여주기
    }

    @GetMapping("/order")
    public void showOrderList(@AuthenticationPrincipal Account account, @RequestBody RequestOrder requestOrder) {
        // READY 상태, COMPLETE 상태 보여주기
    }

}

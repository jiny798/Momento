package jiny.futurevia.service.modules.order.endpoint;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.order.application.CartService;
import jiny.futurevia.service.modules.order.application.OrderService;
import jiny.futurevia.service.modules.order.endpoint.dto.request.RequestOrder;
import jiny.futurevia.service.modules.order.endpoint.dto.request.RequestProduct;
import jiny.futurevia.service.modules.order.endpoint.dto.response.ResponseProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;

    @PostMapping("/order")
    public void order(@AuthenticationPrincipal Account account, @RequestBody RequestOrder requestOrder) {
        orderService.order(account.getId(), requestOrder);
    }

    @GetMapping("/order")
    public void showOrderList(@AuthenticationPrincipal Account account, @RequestBody RequestOrder requestOrder) {
        // READY 상태, COMPLETE 상태 보여주기
    }

    @PostMapping("/cart")
    public void addCart(@AuthenticationPrincipal Account account, @RequestBody RequestProduct requestProduct) {
        cartService.addCart(account.getId(), requestProduct);
    }

    @GetMapping("/cart")
    public List<ResponseProduct> showCartList(@AuthenticationPrincipal Account account, @RequestBody RequestProduct requestProduct) {
        return cartService.getCartList(account.getId());
    }


}

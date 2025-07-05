package jiny.futurevia.service.modules.cart.endpoint;


import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.cart.application.CartService;
import jiny.futurevia.service.modules.cart.endpoint.dto.request.CartRequest;
import jiny.futurevia.service.modules.cart.endpoint.dto.response.CartResponse;
import jiny.futurevia.service.modules.common.response.ApiResponse;
import jiny.futurevia.service.modules.order.endpoint.dto.request.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;


    @PostMapping("/cart")
    public ApiResponse<Void> addCart(@AuthenticationPrincipal Account account, @RequestBody ProductDto productDto) {
        log.debug("add cart {}", productDto);
        cartService.addCart(account.getId(), productDto);
        return ApiResponse.success();
    }

    @GetMapping("/cart")
    public ApiResponse<List<CartResponse>> getCartList(@AuthenticationPrincipal Account account) {
        List<CartResponse> responseProductList = cartService.getCartList(account.getId());
        return ApiResponse.success(responseProductList);
    }

    @DeleteMapping("/cart")
    public ApiResponse<Void> deleteCart(@AuthenticationPrincipal Account account, @RequestBody CartRequest cartRequest) {
        cartService.deleteCart(account.getId(), cartRequest.getId());
        return ApiResponse.success();
    }
}
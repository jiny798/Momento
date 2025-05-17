package jiny.futurevia.service.modules.order.application;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.exception.type.ProductNotFound;
import jiny.futurevia.service.modules.exception.type.UserNotFound;
import jiny.futurevia.service.modules.order.domain.Cart;
import jiny.futurevia.service.modules.order.endpoint.dto.request.RequestProduct;
import jiny.futurevia.service.modules.order.endpoint.dto.response.ResponseProduct;
import jiny.futurevia.service.modules.order.infra.repository.CartRepository;
import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.infra.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;


    @Transactional
    public void addCart(Long userId, RequestProduct requestProduct) {
        Account account = accountRepository.findById(userId).orElseThrow(UserNotFound::new);

        Product product = productRepository.findById(requestProduct.getProductId()).orElseThrow(ProductNotFound::new);
        List<String> flavors = requestProduct.getFlavors();

        Cart cart = Cart.create(account, product.getId(), requestProduct.getCount());
        cart.addOption(flavors);

        cartRepository.save(cart);
    }

    public List<ResponseProduct> getCartList(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(UserNotFound::new);
        List<ResponseProduct> cartList = new ArrayList<>();
        List<Cart> carts = cartRepository.findByAccount(account);
        for (Cart cart : carts) {
            Product product = productRepository.findById(cart.getProductId()).orElseThrow(ProductNotFound::new);
            ResponseProduct responseProduct = ResponseProduct.builder()
                    .productId(product.getId())
                    .productName(product.getTitle())
                    .image(!product.getImageUrls().isEmpty() ? product.getImageUrls().get(0) : null)
                    .price(product.getPrice())
                    .count(cart.getCount())
                    .option(cart.getOption())
                    .build();
            cartList.add(responseProduct);
        }
        return cartList;
    }
}

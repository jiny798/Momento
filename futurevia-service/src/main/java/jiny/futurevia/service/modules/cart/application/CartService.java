package jiny.futurevia.service.modules.cart.application;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.cart.endpoint.dto.response.CartResponse;
import jiny.futurevia.service.modules.cart.support.CartMapper;
import jiny.futurevia.service.modules.order.endpoint.dto.request.ProductDto;
import jiny.futurevia.service.modules.product.exception.ProductNotFound;
import jiny.futurevia.service.modules.account.exception.UserNotFound;
import jiny.futurevia.service.modules.cart.domain.Cart;
import jiny.futurevia.service.modules.order.endpoint.dto.response.ResponseProduct;
import jiny.futurevia.service.modules.order.infra.repository.CartRepository;
import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.infra.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
    private final CartMapper cartMapper;

    @Transactional
    public void addCart(Long userId, ProductDto productDto) {
        Account account = accountRepository.findById(userId).orElseThrow(UserNotFound::new);
        Product product = productRepository.findById(productDto.getProductId()).orElseThrow(ProductNotFound::new);

        Cart cart = Cart.from(account, product, productDto.getOrderCount() ,  productDto.getOptions());
        cartRepository.save(cart);
    }

    public List<CartResponse> getCartList(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(UserNotFound::new);
        List<Cart> carts = cartRepository.findByAccount(account);

        return cartMapper.toDtoList(carts);
    }
}

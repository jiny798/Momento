package jiny.futurevia.service.modules.order.application;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.exception.type.OrderNotFound;
import jiny.futurevia.service.modules.exception.type.ProductNotFound;
import jiny.futurevia.service.modules.exception.type.UserNotFound;
import jiny.futurevia.service.modules.order.domain.Delivery;
import jiny.futurevia.service.modules.order.domain.DeliveryStatus;
import jiny.futurevia.service.modules.order.domain.Order;
import jiny.futurevia.service.modules.order.domain.OrderProduct;
import jiny.futurevia.service.modules.order.infra.repository.OrderRepository;
import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.infra.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Long order(Long userId, Long itemId, int count) {

        Account account = accountRepository.findById(userId).orElseThrow(UserNotFound::new);
        Product product = productRepository.findById(itemId).orElseThrow(ProductNotFound::new);

        Delivery delivery = new Delivery();
        delivery.setAddress(account.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        OrderProduct orderProduct = OrderProduct.createOrderProduct(product, product.getPrice(), count);
        Order order = Order.createOrder(account, delivery, orderProduct);

        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        order.cancel();
    }

}

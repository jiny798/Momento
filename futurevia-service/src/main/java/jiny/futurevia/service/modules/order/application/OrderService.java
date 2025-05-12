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
import jiny.futurevia.service.modules.order.endpoint.dto.request.RequestOrder;
import jiny.futurevia.service.modules.order.endpoint.dto.request.RequestProduct;
import jiny.futurevia.service.modules.order.infra.repository.OrderRepository;
import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.infra.repository.FlavorRepository;
import jiny.futurevia.service.modules.product.infra.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final FlavorRepository flavorRepository;

    @Transactional
    public Long order(Long userId, RequestOrder requestOrders) {
        List<RequestProduct> requestProducts = requestOrders.getRequestProductList();
        Account account = accountRepository.findById(userId).orElseThrow(UserNotFound::new);
        List<OrderProduct> orderProducts = new ArrayList<>();
        Order order = null;
        Delivery delivery = generateDelivery(account, DeliveryStatus.READY);

        for (RequestProduct requestProduct : requestProducts) {
            Product product = productRepository.findById(requestProduct.getProductId()).orElseThrow(ProductNotFound::new);
            List<String> flavors = requestProduct.getFlavors();

            // 주문상품에 대한 정보 저장
            OrderProduct orderProduct = OrderProduct.createOrderProduct(product, product.getPrice(), requestProduct.getCount());
            // 주문상품의 맛 추가
            if (flavors != null && !flavors.isEmpty()) {
                orderProduct.addOption(flavors);
            }
            // 주문상품 추가
            orderProducts.add(orderProduct);
            // 주문 생성
            order = Order.createOrder(account, delivery, orderProducts);

        }

        if (order == null) {
            throw new RuntimeException("주문에 실패하였습니다");
        }

        orderRepository.save(order);
        return order.getId();
    }

    private static Delivery generateDelivery(Account account, DeliveryStatus deliveryStatus) {
        Delivery delivery = new Delivery();
        delivery.setAddress(account.getAddress());
        delivery.setStatus(deliveryStatus);
        return delivery;
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        order.cancel();
    }

}

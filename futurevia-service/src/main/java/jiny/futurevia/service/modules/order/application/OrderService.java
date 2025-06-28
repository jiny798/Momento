package jiny.futurevia.service.modules.order.application;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.order.exception.OrderFailException;
import jiny.futurevia.service.modules.order.exception.OrderNotFound;
import jiny.futurevia.service.modules.product.exception.ProductNotFound;
import jiny.futurevia.service.modules.account.exception.UserNotFound;
import jiny.futurevia.service.modules.order.domain.Delivery;
import jiny.futurevia.service.modules.order.domain.DeliveryStatus;
import jiny.futurevia.service.modules.order.domain.Order;
import jiny.futurevia.service.modules.order.domain.OrderProduct;
import jiny.futurevia.service.modules.order.endpoint.dto.request.OrderDate;
import jiny.futurevia.service.modules.order.endpoint.dto.request.RequestOrder;
import jiny.futurevia.service.modules.order.endpoint.dto.request.RequestProduct;
import jiny.futurevia.service.modules.order.endpoint.dto.response.ResponseOrderProduct;
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
    public Long orderProduct(Long userId, RequestOrder requestOrders) {
        List<RequestProduct> requestProducts = requestOrders.getRequestProductList();
        Account account = accountRepository.findById(userId).orElseThrow(UserNotFound::new);

        Order order = null;
        List<OrderProduct> orderProducts = new ArrayList<>();
        Delivery delivery = generateDelivery(account, DeliveryStatus.READY);

        for (RequestProduct requestProduct : requestProducts) {
            Product product = productRepository.findById(requestProduct.getProductId()).orElseThrow(ProductNotFound::new);
            String optionForProduct = requestProduct.getOptions();

            OrderProduct orderProduct = OrderProduct.createOrderProduct(product, product.getPrice(), requestProduct.getCount());
            orderProduct.addOption(optionForProduct);
            orderProducts.add(orderProduct);

            order = Order.createOrder(account, delivery, orderProducts);

        }

        if (order == null) {
            throw new OrderFailException();
        }

        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFound::new);
        order.cancel();
    }

    public List<ResponseOrderProduct> getOrderProducts(Long accountId, OrderDate orderDate) {
//        Account account = accountRepository.findById(accountId).orElseThrow(UserNotFound::new);
//        LocalDateTime start = orderDate.getStartDate().atStartOfDay();
//        LocalDateTime end = orderDate.getEndDate().atTime(23, 59, 59);
//
//        List<Order> orders = orderRepository.findByAccountIdAndOrderDateBetween(accountId, start, end);
//        List<ResponseOrderProduct> responseOrderProducts = orders.stream()
//                        .map(order -> {
//                            List<Product> products = new ArrayList<>();
//                            List<Integer> counts = new ArrayList<>();
//                            for(OrderProduct orderProduct : order.getOrderProducts()){
//                                products.add(orderProduct.getProduct());
//                                counts.add(orderProduct.getCount());
//                            }
//                            List<String> productImages = products.stream()
//                                    .map(product -> product.getImageUrls().get(0))
//                                    .toList();
//
//                            List<Long> productIds = products.stream()
//                                    .map(Product::getId)
//                                    .toList();
//
//                            List<String> productName = products.stream()
//                                    .map(Product::getTitle)
//                                    .toList();
//
//                            // 이미지 카운트
//                            return new ResponseOrderProduct(
//                                    productImages,
//                                    counts,
//                                    productIds,
//                                    productName,
//                                    order.getOrderDate(),
//                                    order.getStatus(),
//                                    order.getDelivery().getStatus(),
//                                    order.getTotalPrice()
//                            );
//                        }).toList();

//        return responseOrderProducts;
        return null;
    }

    private static Delivery generateDelivery(Account account, DeliveryStatus deliveryStatus) {
        Delivery delivery = new Delivery();
        delivery.setAddress(account.getAddress());
        delivery.setStatus(deliveryStatus);
        return delivery;
    }
}

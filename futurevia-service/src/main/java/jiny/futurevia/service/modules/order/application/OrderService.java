package jiny.futurevia.service.modules.order.application;

import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.order.domain.*;
import jiny.futurevia.service.modules.order.endpoint.dto.request.OrderPeriodRequest;
import jiny.futurevia.service.modules.order.exception.OrderFailException;
import jiny.futurevia.service.modules.order.exception.OrderNotFound;
import jiny.futurevia.service.modules.product.exception.InsufficientStockException;
import jiny.futurevia.service.modules.product.exception.ProductNotFound;
import jiny.futurevia.service.modules.account.exception.UserNotFound;
import jiny.futurevia.service.modules.order.endpoint.dto.request.OrderRequest;
import jiny.futurevia.service.modules.order.endpoint.dto.request.ProductDto;
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
    public Long orderProduct(Long userId, OrderRequest ordersRequest) {
        List<ProductDto> productDtos = ordersRequest.getProductDtoList();
        Account account = accountRepository.findById(userId).orElseThrow(UserNotFound::new);
        Address address = new Address(ordersRequest.getAddressDto().getPostalCode(),
                ordersRequest.getAddressDto().getAddress(),
                ordersRequest.getAddressDto().getDetailAddress());
        Delivery delivery = generateDelivery(account, DeliveryStatus.READY, address);


        Order order = null;
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (ProductDto productDto : productDtos) {
            Product product = productRepository.findById(productDto.getProductId()).orElseThrow(ProductNotFound::new);

            if (product.getStock() < productDto.getCount()) {
                throw new InsufficientStockException(product.getName() + " 상품은 현재 품절입니다.");
            }
            product.decreaseStock(productDto.getCount());

            OrderProduct orderProduct = OrderProduct.createOrderProduct(
                    product,
                    product.getPrice(),
                    productDto.getCount(),
                    productDto.getOption());
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

    public List<ResponseOrderProduct> getOrderProducts(Long accountId, OrderPeriodRequest orderPeriodRequest) {
//        Account account = accountRepository.findById(accountId).orElseThrow(UserNotFound::new);
//        LocalDateTime start = orderDateDto.getStartDate().atStartOfDay();
//        LocalDateTime end = orderDateDto.getEndDate().atTime(23, 59, 59);
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

    private static Delivery generateDelivery(Account account, DeliveryStatus deliveryStatus, Address address) {
        Delivery delivery = new Delivery();
        delivery.setAddress(address);
        delivery.setStatus(deliveryStatus);
        return delivery;
    }
}

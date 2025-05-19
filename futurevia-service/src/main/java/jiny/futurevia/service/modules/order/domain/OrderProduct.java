package jiny.futurevia.service.modules.order.domain;

import jakarta.persistence.*;
import jiny.futurevia.service.modules.product.domain.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@Table(name = "order_product")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class OrderProduct {
    @Id
    @GeneratedValue
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long orderPrice; //주문 가격
    private int count;
    private String option;

    public static OrderProduct createOrderProduct(Product product, Long orderPrice, int count) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setCount(count);
        orderProduct.setOrderPrice(orderPrice);
        return orderProduct;
    }

    public Long getTotalPrice() {
        return getOrderPrice() * getCount();
    }

    public void addOption(String options) {
        this.option = options;
    }
}

package jiny.futurevia.service.modules.order.domain;

import jakarta.persistence.*;
import jiny.futurevia.service.modules.product.domain.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "order_price")
    private BigDecimal orderPrice; //주문 가격

    private int quantity;

    private String option;

    public static OrderProduct createOrderProduct(Product product, BigDecimal orderPrice, int quantity, String option) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setQuantity(quantity);
        orderProduct.setOrderPrice(orderPrice);
        orderProduct.setOption(option);
        return orderProduct;
    }

    public BigDecimal getSubTotalPrice() {
        return getOrderPrice().multiply(new BigDecimal(getQuantity()));
    }

}

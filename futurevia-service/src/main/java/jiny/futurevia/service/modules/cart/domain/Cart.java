package jiny.futurevia.service.modules.cart.domain;

import jakarta.persistence.*;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.common.entity.AuditingEntity;
import jiny.futurevia.service.modules.product.domain.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Cart extends AuditingEntity {
    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Integer quantity;

    private String options;

    public static Cart from(Account account, Product product, Integer quantity, String options) {
        Cart cart = new Cart();
        cart.setAccount(account);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cart.setOptions(options);
        return cart;
    }

}

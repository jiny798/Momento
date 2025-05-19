package jiny.futurevia.service.modules.order.domain;

import jakarta.persistence.*;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Cart {
    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    private Long productId;

    private Integer count;

    private String option;

    @ManyToOne
    private Account account;

    public static Cart create(Account account, Long productId, Integer count) {
        Cart cart = new Cart();
        cart.setAccount(account);
        cart.setProductId(productId);
        cart.setCount(count);
        cart.setOption("");
        return cart;
    }

    public void addOption(String options) {
        option = options;
    }

}

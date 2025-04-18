package jiny.futurevia.service.modules.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.product.endpoint.dto.request.ProductCreate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    Account account;

    @NotBlank
    @Size(min = 5, max = 255, message = "최소 5자 이상 입력주세요")
    private String title;

    @NotNull
    private Long price;

    @NotNull
    private Long stockQuantity;

    @NotNull
    @Lob
    private String details;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static Product from(ProductCreate productCreate) {
        Product product = new Product();
        product.title = productCreate.getTitle();
        product.price = productCreate.getPrice();
        product.stockQuantity = productCreate.getStockQuantity();
        product.details = productCreate.getDetails();
        product.createdAt = LocalDateTime.now();
        return product;
    }



}

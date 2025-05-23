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
import java.util.ArrayList;
import java.util.List;

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
    @Size(min = 1, max = 255, message = "최소 1자 이상 입력주세요")
    private String title;

    @NotNull
    private Long price;

    private Integer optionCount;

    @NotNull
    @Lob
    private String details;

    private List<String> imageUrls = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private boolean active;

    @ManyToOne
    private Category category;


    public static Product from(ProductCreate productCreate) {
        Product product = new Product();
        product.title = productCreate.getTitle();
        product.price = productCreate.getPrice();
        product.details = productCreate.getDetails();
        product.imageUrls = productCreate.getImageUrls();
        product.createdAt = LocalDateTime.now();
        product.active = true;
        product.optionCount = productCreate.getOptionCount();
        return product;
    }




}

package jiny.futurevia.service.modules.product.domain;

import jakarta.persistence.*;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.common.entity.AuditingEntity;
import jiny.futurevia.service.modules.product.exception.InvalidProductException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends AuditingEntity {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Lob
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(name = "flavor_select_count")
    private Integer flavorSelectCount;

    @OneToMany(mappedBy = "product")
    private List<ProductImages> productImages = new ArrayList<>();

    @Column(nullable = false)
    private boolean active;

    public static Product create(final String name,
                                 final String description,
                                 final Long price,
                                 final List<String> imageUrls,
                                 final Account account,
                                 final Category category,
                                 final Integer flavorSelectCount
    ) {
        Product product = new Product();
        product.validateProduct(name, description, price);
        product.name = name;
        product.description = description;
        product.price = price;
        product.addImages(imageUrls);
        product.account = account;
        product.category = category;
        product.flavorSelectCount = flavorSelectCount;
        product.active = true;

        return product;
    }

    private void validateProduct(String name, String description, Long price) {
        if (name == null || name.isBlank()) {
            throw new InvalidProductException("상품 이름은 필수입니다.");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidProductException("상품 설명은 필수입니다.");
        }
        if (price == null || price <= 0) {
            throw new InvalidProductException("상품 가격은 0보다 커야 합니다.");
        }
    }

    private void addImages(List<String> images) {
        for (String imageUrl : images) {
            ProductImages productImages = ProductImages.of(this, imageUrl);
            this.productImages.add(productImages);
        }
    }


}

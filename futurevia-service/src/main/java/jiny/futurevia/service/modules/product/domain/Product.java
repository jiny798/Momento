package jiny.futurevia.service.modules.product.domain;

import jakarta.persistence.*;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.common.AuditingEntity;
import jiny.futurevia.service.modules.product.endpoint.dto.request.RequestProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Product extends AuditingEntity {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    private Long price;

    private Long stock;

    @ManyToOne(fetch = FetchType.LAZY)
    Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(name = "flavor_select_count")
    private Integer flavorSelectCount;

    @OneToMany(mappedBy = "product")
    private List<ProductImages> productImages = new ArrayList<>();

    private boolean active;

    public static Product create(String name,
                                 String description,
                                 Long price,
                                 Long stock,
                                 List<String> imageUrls,
                                 Account account,
                                 Category category,
                                 Integer flavorSelectCount
    ) {
        Product product = new Product();
        product.name = name;
        product.description = description;
        product.price = price;
        product.stock = stock;
        product.addImages(imageUrls);
        product.account = account;
        product.category = category;
        product.flavorSelectCount = flavorSelectCount;
        product.active = true;

        return product;
    }

    private void addImages(List<String> images) {
        for (String imageUrl : images) {
            ProductImages productImages = ProductImages.of(this, imageUrl);
            this.productImages.add(productImages);
        }
    }


}

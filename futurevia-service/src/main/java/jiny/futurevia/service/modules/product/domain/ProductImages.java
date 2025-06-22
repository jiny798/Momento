package jiny.futurevia.service.modules.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "image_url")
    private String imageUrl;

    public static ProductImages of(Product product, String imageUrl) {
        ProductImages productImages = new ProductImages();
        productImages.product = product;
        productImages.imageUrl = imageUrl;
        return productImages;
    }
}

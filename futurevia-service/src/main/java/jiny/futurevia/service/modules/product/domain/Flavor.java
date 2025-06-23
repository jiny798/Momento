package jiny.futurevia.service.modules.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Flavor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean active;

    public static Flavor from(String  name){
        Flavor flavor = new Flavor();
        flavor.name = name;
        return flavor;
    }
}

package jiny.futurevia.service.modules.cart.endpoint.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Data
public class CartRequest {

    private  Long id;

    @NotNull
    private Long accountId;

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

    private String options;
}
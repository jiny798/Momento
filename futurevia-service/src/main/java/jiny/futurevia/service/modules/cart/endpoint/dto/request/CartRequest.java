package jiny.futurevia.service.modules.cart.endpoint.dto.request;

@Getter
public class CartRequest {

    @NotNull
    private Long accountId;

    @NotNull
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    private String options;
}
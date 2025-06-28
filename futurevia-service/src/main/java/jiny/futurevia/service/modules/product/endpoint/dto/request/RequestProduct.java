package jiny.futurevia.service.modules.product.endpoint.dto.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestProduct {

    @NotBlank(message = "최소 1자 이상 입력주세요")
    @Size(min = 1, max = 255, message = "최소 1자 이상 입력주세요")
    private String name;

    @NotNull
    private Long price;

    @NotNull
    private Integer stock;

    private Long categoryId;

    private Integer flavorSelectCount;

    @NotNull
    @Lob
    private String description;

    private List<String> productImages;

}
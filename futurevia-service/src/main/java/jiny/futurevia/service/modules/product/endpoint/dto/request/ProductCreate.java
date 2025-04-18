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
public class ProductCreate {

	@NotBlank
	@Size(min = 5, max = 255, message = "최소 5자 이상 입력주세요")
	private String title;

	@NotNull
	private Long price;

	private Long minOrderQuantity;

	private Long stockQuantity;

	@NotNull
	@Lob
	private String details;

	private List<String> imageUrls;

}
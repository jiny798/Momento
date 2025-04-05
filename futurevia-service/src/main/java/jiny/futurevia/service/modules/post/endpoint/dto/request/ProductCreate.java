package jiny.futurevia.service.modules.post.endpoint.dto.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


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

	@NotNull
	private String shippingMethod;

	@NotNull
	private Long shippingFee;

	@NotNull
	private Long minOrderQuantity;

	@NotNull
	private Long stockQuantity;

	@NotNull
	@Lob
	private String details;

	@NotNull
	private Boolean isDefect;

}
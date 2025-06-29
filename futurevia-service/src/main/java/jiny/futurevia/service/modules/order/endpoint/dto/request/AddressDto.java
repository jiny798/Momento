package jiny.futurevia.service.modules.order.endpoint.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDto {
    private String postalCode;
    private String address;
    private String detailAddress;
}

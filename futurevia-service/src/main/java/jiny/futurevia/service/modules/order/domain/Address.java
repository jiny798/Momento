package jiny.futurevia.service.modules.order.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String postalCode;
    private String address;
    private String detailAddress;

    public Address(final String postalCode, final String address, final String detailAddress) {
        this.postalCode = postalCode;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    protected Address() {

    }
}
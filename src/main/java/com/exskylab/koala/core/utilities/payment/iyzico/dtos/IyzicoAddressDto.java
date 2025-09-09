package com.exskylab.koala.core.utilities.payment.iyzico.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IyzicoAddressDto {

    private String address;
    private String zipCode;
    private String contactName;
    private String city;
    private String country;

}

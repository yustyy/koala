package com.exskylab.koala.core.utilities.payment.iyzico.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubMerchantUpdateRequestDto {

    @NotBlank
    private String subMerchantKey;

    private String name;
    private String email;
    private String gsmNumber;
    private String address;
    private String iban;
    private String contactName;
    private String contactSurname;
    private String currency;
    private String identityNumber;
}

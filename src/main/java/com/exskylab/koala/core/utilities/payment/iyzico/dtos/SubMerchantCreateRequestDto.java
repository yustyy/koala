package com.exskylab.koala.core.utilities.payment.iyzico.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubMerchantCreateRequestDto {

    @NotBlank
    private String subMerchantExternalId;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String gsmNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String iban;

    @NotNull
    private SubMerchantType subMerchantType;

    //personal
    private String identityNumber;
    private String contactName;
    private String contactSurname;

    //company
    private String taxNumber;
    private String legalCompanyTitle;
    private String taxOffice;

}

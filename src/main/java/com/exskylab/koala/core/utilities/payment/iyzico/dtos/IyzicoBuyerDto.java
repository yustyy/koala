package com.exskylab.koala.core.utilities.payment.iyzico.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IyzicoBuyerDto {

    private String id; //user or company ID

    private String name;

    private String surname;

    private String gsmNumber;

    private String email;

    private String identityNumber;

    private String ip;

    private String city;

    private String country;

    private String registrationAddress;


}

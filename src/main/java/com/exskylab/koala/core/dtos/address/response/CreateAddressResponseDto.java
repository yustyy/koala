package com.exskylab.koala.core.dtos.address.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressResponseDto {

    private UUID id;

    private String countryName;

    private String countryCode;

    private String cityName;

    private String districtName;

    private String addressLine;

    private String postalCode;

    private double latitude;

    private double longitude;

}

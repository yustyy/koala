package com.exskylab.koala.core.dtos.address.request;

import com.exskylab.koala.core.constants.AddressMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequestDto {

    @NotBlank(message = AddressMessages.COUNTRY_NAME_NOT_BLANK)
    private String countryName;

    @NotBlank(message = AddressMessages.COUNTRY_CODE_NOT_BLANK)
    @Size(min = 2, max = 3, message = AddressMessages.COUNTRY_CODE_MIN_MAX_LENGTH)
    private String countryCode;

    @NotBlank(message = AddressMessages.CITY_NAME_NOT_BLANK)
    private String cityName;

    //optional
    private String districtName;

    @NotBlank(message = AddressMessages.ADDRESS_LINE_NOT_BLANK)
    private String addressLine;

    //optional
    private String postalCode;

    @NotNull(message = AddressMessages.LATITUDE_NOT_NULL)
    private Double latitude;

    @NotNull(message = AddressMessages.LONGITUDE_NOT_NULL)
    private Double longitude;

}


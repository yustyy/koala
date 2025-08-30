package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.address.response.AddressDto;
import com.exskylab.koala.entities.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {



    public AddressDto toAddressDto(Address address){
        if (address == null){
            return null;
        }
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setCountryName(address.getCity().getCountry().getName());
        dto.setCountryCode(address.getCity().getCountry().getCode());
        dto.setCityName(address.getCity().getName());
        dto.setDistrictName(address.getDistrict().getName());
        dto.setAddressLine(address.getAddressLine());
        dto.setPostalCode(address.getPostalCode());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());
        return dto;

    }


}

package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.address.request.CreateAddressRequestDto;
import com.exskylab.koala.entities.Address;

public interface AddressService {
    Address createAddress(CreateAddressRequestDto createAddressRequestDto);
}

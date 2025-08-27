package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.AddressService;
import com.exskylab.koala.core.dtos.address.request.CreateAddressRequestDto;
import com.exskylab.koala.entities.Address;
import org.springframework.stereotype.Service;

@Service
public class AddressManager implements AddressService {
    @Override
    public Address createAddress(CreateAddressRequestDto createAddressRequestDto) {
        return null;
    }
}

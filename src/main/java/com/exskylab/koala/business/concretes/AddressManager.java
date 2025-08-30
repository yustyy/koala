package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.AddressService;
import com.exskylab.koala.core.constants.AddressMessages;
import com.exskylab.koala.core.dtos.address.request.CreateAddressRequestDto;
import com.exskylab.koala.dataAccess.AddressDao;
import com.exskylab.koala.dataAccess.CityDao;
import com.exskylab.koala.dataAccess.CountryDao;
import com.exskylab.koala.dataAccess.DistrictDao;
import com.exskylab.koala.entities.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AddressManager implements AddressService {

    private final AddressDao addressDao;
    private final Logger logger = LoggerFactory.getLogger(AddressManager.class);

    private final CountryDao countryDao;
    private final CityDao cityDao;
    private final DistrictDao districtDao;

    public AddressManager(AddressDao addressDao, CountryDao countryDao, CityDao cityDao, DistrictDao districtDao) {
        this.addressDao = addressDao;
        this.countryDao = countryDao;
        this.cityDao = cityDao;
        this.districtDao = districtDao;
    }

    @Override
    public Address createAddress(CreateAddressRequestDto createAddressRequestDto) {
        logger.info("Creating address...");
        var country = countryDao.findByCodeAllIgnoreCase(createAddressRequestDto.getCountryCode())
                .orElseThrow(() -> {
                    logger.error("Country with code: {} not found", createAddressRequestDto.getCountryCode());
                    return new IllegalArgumentException(AddressMessages.COUNTRY_NOT_FOUND);
                });

        var city = cityDao.findByNameAndCountryAllIgnoreCase(createAddressRequestDto.getCityName(), country)
                .orElseThrow(() -> {
                    logger.error("City with name: {} not found in country: {}", createAddressRequestDto.getCityName(), country.getName());
                    return new IllegalArgumentException(AddressMessages.CITY_NOT_FOUND);
                });

        var district = districtDao.findByNameAndCityAllIgnoreCase(createAddressRequestDto.getDistrictName(), city)
                .orElseThrow(() -> {
                    logger.error("District with name: {} not found in city: {}", createAddressRequestDto.getDistrictName(), city.getName());
                    return new IllegalArgumentException(AddressMessages.DISTRICT_NOT_FOUND);
                });

        Address address = new Address();
        address.setCountry(country);
        address.setCity(city);
        address.setDistrict(district);
        address.setAddressLine(createAddressRequestDto.getAddressLine());
        address.setPostalCode(createAddressRequestDto.getPostalCode());
        address.setLatitude(createAddressRequestDto.getLatitude());
        address.setLongitude(createAddressRequestDto.getLongitude());


        Address savedAddress = addressDao.save(address);

        logger.info("Saved address with id: {}", savedAddress.getId());

        return savedAddress;
    }
}

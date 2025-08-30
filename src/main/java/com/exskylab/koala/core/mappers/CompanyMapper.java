package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.company.response.CompanyDto;
import com.exskylab.koala.core.dtos.company.response.OnlyCompanyDto;
import com.exskylab.koala.core.properties.R2Properties;
import com.exskylab.koala.entities.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    private final R2Properties r2Properties;
    private final CompanyContactMapper companyContactMapper;
    private final AddressMapper addressMapper;

    public CompanyMapper(R2Properties r2Properties, CompanyContactMapper companyContactMapper, AddressMapper addressMapper) {
        this.r2Properties = r2Properties;
        this.companyContactMapper = companyContactMapper;
        this.addressMapper = addressMapper;
    }

    public CompanyDto toCompanyDto(Company company) {

        var companyDto = new CompanyDto();
        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setEmail(company.getEmail());
        companyDto.setLogoUrl(company.getLogo() != null ? r2Properties.getPublicUrl()+"/"+company.getLogo().getFileUrl() : null);
        companyDto.setPhoneNumber(company.getPhoneNumber());
        companyDto.setWebsite(company.getWebsite());
        companyDto.setDescription(company.getDescription());
        companyDto.setTaxNumber(company.getTaxNumber());
        companyDto.setType(company.getType());
        companyDto.setCompanyContacts(companyContactMapper.toCompanyContactWithoutCompanyDtoList(company.getContacts()));
        companyDto.setAddress(addressMapper.toAddressDto(company.getAddress()));

        return companyDto;
    }

    public OnlyCompanyDto toOnlyCompanyDto(Company company) {
        OnlyCompanyDto dto = new OnlyCompanyDto();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setEmail(company.getEmail());
        dto.setLogoUrl(company.getLogo() != null ? r2Properties.getPublicUrl()+"/"+company.getLogo().getFileUrl() : null);
        dto.setPhoneNumber(company.getPhoneNumber());
        dto.setWebsite(company.getWebsite());
        dto.setDescription(company.getDescription());
        dto.setTaxNumber(company.getTaxNumber());
        dto.setType(company.getType());
        dto.setAddress(addressMapper.toAddressDto(company.getAddress()));
        return dto;
    }
}

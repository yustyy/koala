package com.exskylab.koala.core.mappers.company;

import com.exskylab.koala.core.dtos.company.response.CompanyDto;
import com.exskylab.koala.core.mappers.companyContact.CompanyContactMapper;
import com.exskylab.koala.core.properties.R2Properties;
import com.exskylab.koala.entities.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    private final R2Properties r2Properties;
    private final CompanyContactMapper companyContactMapper;

    public CompanyMapper(R2Properties r2Properties, CompanyContactMapper companyContactMapper) {
        this.r2Properties = r2Properties;
        this.companyContactMapper = companyContactMapper;
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

        return companyDto;
    }
}

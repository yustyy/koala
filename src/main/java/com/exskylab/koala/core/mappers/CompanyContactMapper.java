package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.companyContact.response.CompanyContactWithoutCompanyDto;
import com.exskylab.koala.entities.CompanyContact;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyContactMapper {

    private final UserMapper userMapper;

    public CompanyContactMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CompanyContactWithoutCompanyDto toCompanyContactWithoutCompanyDto(CompanyContact contact) {
        var companyContactDto = new CompanyContactWithoutCompanyDto();
        companyContactDto.setId(contact.getId());
        companyContactDto.setUser(userMapper.toSafeUserDto(contact.getUser()));
        companyContactDto.setRole(contact.getRole());
        return companyContactDto;
    }

    public List<CompanyContactWithoutCompanyDto> toCompanyContactWithoutCompanyDtoList(List<CompanyContact> contacts) {
        return contacts.stream()
                .map(this::toCompanyContactWithoutCompanyDto)
                .collect(Collectors.toList());
    }


}

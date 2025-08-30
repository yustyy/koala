package com.exskylab.koala.core.dtos.company.response;

import com.exskylab.koala.core.dtos.address.response.AddressDto;
import com.exskylab.koala.core.dtos.companyContact.response.CompanyContactWithoutCompanyDto;
import com.exskylab.koala.entities.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    private UUID id;

    private String name;

    private String email;

    private String logoUrl;

    private String phoneNumber;

    private String website;

    private String description;

    private String taxNumber;

    private CompanyType type;

    private AddressDto address;

    private List<CompanyContactWithoutCompanyDto> companyContacts;

}

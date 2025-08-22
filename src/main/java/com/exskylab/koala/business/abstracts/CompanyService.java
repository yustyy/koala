package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.company.request.CreateCompanyRequestDto;
import com.exskylab.koala.core.dtos.companyContact.request.InviteContactToCompanyDto;
import com.exskylab.koala.entities.Company;
import com.exskylab.koala.entities.CompanyContactInvitation;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CompanyService {


    Company addCompany(@Valid CreateCompanyRequestDto createCompanyRequestDto, MultipartFile logo);

    void inviteContactToCompany(UUID companyId, InviteContactToCompanyDto contactToCompanyDto);

    List<CompanyContactInvitation> getCompanyContactInvitations(UUID companyId);

}

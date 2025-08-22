package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.companyContact.request.InviteContactToCompanyDto;
import com.exskylab.koala.entities.Company;
import com.exskylab.koala.entities.CompanyContact;
import com.exskylab.koala.entities.CompanyContactRole;
import com.exskylab.koala.entities.User;
import jakarta.validation.constraints.NotBlank;

public interface CompanyContactService {

    void inviteContactToCompany(InviteContactToCompanyDto inviteContactToCompanyDto);

    CompanyContact addContactToCompany(Company company, User userToInvite, CompanyContactRole role);

}

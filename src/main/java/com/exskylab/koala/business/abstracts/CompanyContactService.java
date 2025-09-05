package com.exskylab.koala.business.abstracts;


import com.exskylab.koala.entities.Company;
import com.exskylab.koala.entities.CompanyContact;
import com.exskylab.koala.entities.CompanyContactRole;
import com.exskylab.koala.entities.User;

import java.util.UUID;

public interface CompanyContactService {
    CompanyContact addCompanyContact(Company company, User invitedUser, CompanyContactRole role);

    boolean isUserAContactOfCompany(UUID companyId, UUID userId);

    boolean isUserAdminOfCompany(UUID companyId, UUID userId);
}

package com.exskylab.koala.business.abstracts;


import com.exskylab.koala.entities.Company;
import com.exskylab.koala.entities.CompanyContact;
import com.exskylab.koala.entities.CompanyContactRole;
import com.exskylab.koala.entities.User;

public interface CompanyContactService {
    CompanyContact addCompanyContact(Company company, User invitedUser, CompanyContactRole role);
}

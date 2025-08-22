package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.CompanyContactService;
import com.exskylab.koala.dataAccess.CompanyContactDao;
import com.exskylab.koala.entities.Company;
import com.exskylab.koala.entities.CompanyContact;
import com.exskylab.koala.entities.CompanyContactRole;
import com.exskylab.koala.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CompanyContactManager implements CompanyContactService {

    private final CompanyContactDao companyContactDao;
    private final static Logger logger = LoggerFactory.getLogger(CompanyContactManager.class);

    public CompanyContactManager(CompanyContactDao companyContactDao) {
        this.companyContactDao = companyContactDao;
    }


    @Override
    public CompanyContact addCompanyContact(Company company, User invitedUser, CompanyContactRole role) {
        logger.info("Adding company contact for company with id: {}, invited user with id: {}, role: {}", company.getId(), invitedUser.getId(), role);

        CompanyContact companyContact = new CompanyContact();
        companyContact.setCompany(company);
        companyContact.setUser(invitedUser);
        companyContact.setRole(role);
        CompanyContact savedContact = companyContactDao.save(companyContact);

        logger.info("Saved company contact with id: {}", savedContact.getId());
        return savedContact;
    }
}

package com.exskylab.koala.business.concretes;

import com.exskylab.koala.business.abstracts.CompanyContactService;
import com.exskylab.koala.dataAccess.CompanyContactDao;
import com.exskylab.koala.entities.CompanyContact;
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
    public CompanyContact addCompanyContact(CompanyContact companyContact) {
        return null;
    }
}

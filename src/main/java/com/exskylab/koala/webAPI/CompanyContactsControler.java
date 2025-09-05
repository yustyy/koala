package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.CompanyContactService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company-contacts")
public class CompanyContactsControler {

    private final CompanyContactService companyContactService;


    public CompanyContactsControler(CompanyContactService companyContactService) {
        this.companyContactService = companyContactService;
    }

}

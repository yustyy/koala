package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.CompanyContactService;
import com.exskylab.koala.core.constants.CompanyContactMessages;
import com.exskylab.koala.core.dtos.companyContact.request.InviteContactToCompanyDto;
import com.exskylab.koala.core.utilities.results.SuccessResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companyContacts")
public class CompanyContactsControler {

    private final CompanyContactService companyContactService;


    public CompanyContactsControler(CompanyContactService companyContactService) {
        this.companyContactService = companyContactService;
    }


    @PostMapping("/addContactToCompany")
    public ResponseEntity<SuccessResult> addContactToCompany(@RequestBody @Valid InviteContactToCompanyDto inviteContactToCompanyDto, HttpServletRequest request){

        companyContactService.inviteContactToCompany(inviteContactToCompanyDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResult(
                        CompanyContactMessages.CONTACT_INVITED_SUCCESS,
                        HttpStatus.OK,
                        request.getRequestURI()
                        ));



    }
}

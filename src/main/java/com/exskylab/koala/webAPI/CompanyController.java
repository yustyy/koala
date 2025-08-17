package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.CompanyService;
import com.exskylab.koala.core.constants.CompanyMessages;
import com.exskylab.koala.core.dtos.company.request.CreateCompanyRequestDto;
import com.exskylab.koala.core.dtos.company.response.CompanyDto;
import com.exskylab.koala.core.mappers.company.CompanyMapper;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;


    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }


    @PostMapping(value = "/addCompany", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessDataResult<CompanyDto>> addCompany(
            @RequestPart("company") @Valid CreateCompanyRequestDto createCompanyRequestDto,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            HttpServletRequest request){

        var response = companyService.addCompany(createCompanyRequestDto, logo);

        var dto = companyMapper.toCompanyDto(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessDataResult<CompanyDto>(dto, CompanyMessages.COMPANY_ADDED_SUCCESS, HttpStatus.CREATED, request.getRequestURI())
        );

    }

}

package com.exskylab.koala.webAPI;

import com.exskylab.koala.business.abstracts.CompanyService;
import com.exskylab.koala.business.abstracts.JobService;
import com.exskylab.koala.core.constants.CompanyMessages;
import com.exskylab.koala.core.dtos.company.request.CreateCompanyRequestDto;
import com.exskylab.koala.core.dtos.company.response.CompanyDto;
import com.exskylab.koala.core.dtos.companyContact.request.InviteContactToCompanyDto;
import com.exskylab.koala.core.dtos.companyContactInvitation.response.CompanyContactInvitationDto;
import com.exskylab.koala.core.dtos.job.request.CompaniesCompanyIdJobsPostRequestDto;
import com.exskylab.koala.core.dtos.job.response.CompaniesCompanyIdJobsPostResponseDto;
import com.exskylab.koala.core.mappers.CompanyContactInvitationMapper;
import com.exskylab.koala.core.mappers.CompanyMapper;
import com.exskylab.koala.core.mappers.JobMapper;
import com.exskylab.koala.core.utilities.results.SuccessDataResult;
import com.exskylab.koala.core.utilities.results.SuccessResult;
import com.exskylab.koala.entities.Company;
import com.exskylab.koala.entities.CompanyContactInvitation;
import com.exskylab.koala.entities.Job;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final CompanyContactInvitationMapper companyContactInvitationMapper;
    private final JobService jobService;
    private final JobMapper jobMapper;


    public CompanyController(CompanyService companyService, CompanyMapper companyMapper, CompanyContactInvitationMapper companyContactInvitationMapper, JobService jobService, JobMapper jobMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
        this.companyContactInvitationMapper = companyContactInvitationMapper;
        this.jobService = jobService;
        this.jobMapper = jobMapper;
    }


    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessDataResult<CompanyDto>> addCompany(
            @RequestPart("company") @Valid CreateCompanyRequestDto createCompanyRequestDto,
            @RequestPart(value = "logo", required = false) MultipartFile logo){

        var response = companyService.addCompany(createCompanyRequestDto, logo);

        var dto = companyMapper.toCompanyDto(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessDataResult<CompanyDto>(dto, CompanyMessages.COMPANY_ADDED_SUCCESS, HttpStatus.CREATED)
        );

    }


    @PostMapping("/{companyId}/contact-invitations")
    public ResponseEntity<SuccessResult> sendCompanyContactInvitation(@PathVariable UUID companyId, @RequestBody @Valid InviteContactToCompanyDto contactToCompanyDto){

        companyService.inviteContactToCompany(companyId, contactToCompanyDto);


        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessResult(CompanyMessages.COMPANY_CONTACT_INVITATION_SENT_SUCCESS,
                        HttpStatus.CREATED));

    }

    @GetMapping("/{companyId}/contact-invitations")
    public ResponseEntity<SuccessDataResult<List<CompanyContactInvitationDto>>> getCompanyContactInvitations(@PathVariable UUID companyId){
        List<CompanyContactInvitation> response = companyService.getCompanyContactInvitations(companyId);


        var dtos = companyContactInvitationMapper.toCompanyContactInvitationDtoList(response);

        return ResponseEntity.status(HttpStatus.OK).body(
                new SuccessDataResult<List<CompanyContactInvitationDto>>(dtos,
                        CompanyMessages.COMPANY_CONTACT_INVITATIONS_LISTED_SUCCESS,
                        HttpStatus.OK

                ));


    }


    @PostMapping("/{companyId}/jobs")
    public ResponseEntity<SuccessDataResult<CompaniesCompanyIdJobsPostResponseDto>> createJobForCompany(@PathVariable String companyId,
                                                                                                        @RequestBody @Valid CompaniesCompanyIdJobsPostRequestDto companiesCompanyIdJobsPostRequestDto){
        Job response = jobService.createCorporateJob(companyId, companiesCompanyIdJobsPostRequestDto);


        var dto = jobMapper.toCompaniesCompanyIdJobsPostResponseDto(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SuccessDataResult<CompaniesCompanyIdJobsPostResponseDto>(dto,
                        CompanyMessages.JOB_CREATED_SUCCESS,
                        HttpStatus.CREATED
                ));
    }

}

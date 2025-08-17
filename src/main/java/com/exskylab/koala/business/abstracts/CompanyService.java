package com.exskylab.koala.business.abstracts;

import com.exskylab.koala.core.dtos.company.request.CreateCompanyRequestDto;
import com.exskylab.koala.entities.Company;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

public interface CompanyService {


    Company addCompany(@Valid CreateCompanyRequestDto createCompanyRequestDto, MultipartFile logo);
}

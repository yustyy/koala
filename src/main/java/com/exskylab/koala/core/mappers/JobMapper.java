package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.job.response.CompaniesCompanyIdJobsPostResponseDto;
import com.exskylab.koala.core.dtos.job.response.UsersMeJobsPostResponseDto;
import com.exskylab.koala.entities.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    private final UserMapper userMapper;
    private final JobCategoryMapper jobCategoryMapper;
    private final AddressMapper addressMapper;
    private final CompanyMapper companyMapper;

    public JobMapper(UserMapper userMapper, JobCategoryMapper jobCategoryMapper, AddressMapper addressMapper, CompanyMapper companyMapper) {
        this.userMapper = userMapper;
        this.jobCategoryMapper = jobCategoryMapper;
        this.addressMapper = addressMapper;
        this.companyMapper = companyMapper;
    }

    public UsersMeJobsPostResponseDto toUsersMeJobsPostResponseDto(Job job) {

        UsersMeJobsPostResponseDto dto = new UsersMeJobsPostResponseDto();
        dto.setId(job.getId());
        dto.setCreator(userMapper.toSafeUserDto(job.getCreator()));
        dto.setCategory(jobCategoryMapper.toCategoryDto(job.getCategory()));
        dto.setEmployerType(job.getEmployerType());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setSector(job.getSector());
        dto.setPosition(job.getPosition());
        dto.setDuties(job.getDuties());
        dto.setInsuranceRequired(job.isInsuranceRequired());
        dto.setAddress(addressMapper.toAddressDto(job.getAddress()));
        dto.setType(job.getType());
        dto.setStartDateTime(job.getStartDateTime());
        dto.setEndDateTime(job.getEndDateTime());
        dto.setSalaryType(job.getSalaryType());
        dto.setSalary(job.getSalary());
        dto.setMinAge(job.getMinAge());
        dto.setMaxAge(job.getMaxAge());
        dto.setExperienceRequired(job.isExperienceRequired());
        dto.setDressCode(job.getDressCode());
        dto.setStatus(job.getStatus());
        return dto;

    }


    public CompaniesCompanyIdJobsPostResponseDto toCompaniesCompanyIdJobsPostResponseDto(Job job) {

        CompaniesCompanyIdJobsPostResponseDto dto = new CompaniesCompanyIdJobsPostResponseDto();
        dto.setId(job.getId());
        dto.setCompany(companyMapper.toOnlyCompanyDto(job.getCompany()));
        dto.setCreator(userMapper.toSafeUserDto(job.getCreator()));
        dto.setCategory(jobCategoryMapper.toCategoryDto(job.getCategory()));
        dto.setEmployerType(job.getEmployerType());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setSector(job.getSector());
        dto.setPosition(job.getPosition());
        dto.setDuties(job.getDuties());
        dto.setInsuranceRequired(job.isInsuranceRequired());
        dto.setAddress(addressMapper.toAddressDto(job.getAddress()));
        dto.setType(job.getType());
        dto.setStartDateTime(job.getStartDateTime());
        dto.setEndDateTime(job.getEndDateTime());
        dto.setSalaryType(job.getSalaryType());
        dto.setSalary(job.getSalary());
        dto.setMinAge(job.getMinAge());
        dto.setMaxAge(job.getMaxAge());
        dto.setExperienceRequired(job.isExperienceRequired());
        dto.setDressCode(job.getDressCode());
        dto.setStatus(job.getStatus());
        return dto;
    }
}

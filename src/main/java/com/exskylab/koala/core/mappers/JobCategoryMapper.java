package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.jobCategory.response.JobCategoryDto;
import com.exskylab.koala.entities.JobCategory;
import org.springframework.stereotype.Component;

@Component
public class JobCategoryMapper {


    public JobCategoryDto toCategoryDto(JobCategory jobCategory) {

        JobCategoryDto dto = new JobCategoryDto();
        dto.setId(jobCategory.getId());
        dto.setName(jobCategory.getName());
        dto.setRequiresInsurance(jobCategory.isRequiresInsurance());
        return dto;

    }



}

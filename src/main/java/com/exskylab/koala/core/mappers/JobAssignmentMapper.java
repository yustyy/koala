package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.jobAssignment.response.JobAssignmentsAssignmentIdStatusPatchResponseDto;
import com.exskylab.koala.core.dtos.jobAssignment.response.ShortJobAssignmentDto;
import com.exskylab.koala.entities.JobAssignment;
import org.springframework.stereotype.Component;

@Component
public class JobAssignmentMapper {

    private final JobMapper jobMapper;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;

    public JobAssignmentMapper(JobMapper jobMapper, UserMapper userMapper, CompanyMapper companyMapper) {
        this.jobMapper = jobMapper;
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
    }

    public ShortJobAssignmentDto toShortJobAssignmentDto(JobAssignment jobAssignment){
        ShortJobAssignmentDto dto = new ShortJobAssignmentDto();
        dto.setId(jobAssignment.getId());
        dto.setStatus(jobAssignment.getStatus());
        dto.setAssignmentDateTime(jobAssignment.getAssignmentDateTime());

        return dto;
    }

    public JobAssignmentsAssignmentIdStatusPatchResponseDto toJobAssignmentsAssignmentIdStatusPatchResponseDto(JobAssignment jobAssignment){
        JobAssignmentsAssignmentIdStatusPatchResponseDto dto = new JobAssignmentsAssignmentIdStatusPatchResponseDto();
        dto.setId(jobAssignment.getId());
        dto.setAssignmentDateTime(jobAssignment.getAssignmentDateTime());
        dto.setStatus(jobAssignment.getStatus());
        dto.setJob(jobMapper.toShortJobDto(jobAssignment.getJob()));
        dto.setWorker(userMapper.toSafeUserDto(jobAssignment.getWorker()));
        dto.setCompany(companyMapper.toOnlyCompanyDto(jobAssignment.getJob().getCompany()));
        return dto;
    }

}

package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.jobApplication.request.JobApplicationsApplicationIdPatchRequestDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobApplicationsApplicationIdPatchResponseDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobsJobIdApplicationsGetResponseDto;
import com.exskylab.koala.core.dtos.jobApplication.response.JobsJobIdApplicationsPostResponseDto;
import com.exskylab.koala.entities.JobApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobApplicationMapper {

    private final UserMapper userMapper;
    private final JobMapper jobMapper;
    private final JobAssignmentMapper jobAssignmentMapper;


    public JobApplicationMapper(UserMapper userMapper, JobMapper jobMapper, JobAssignmentMapper jobAssignmentMapper) {
        this.userMapper = userMapper;
        this.jobMapper = jobMapper;
        this.jobAssignmentMapper = jobAssignmentMapper;
    }


    public JobsJobIdApplicationsPostResponseDto toJobsJobIdApplicationsPostResponseDto(JobApplication jobApplication) {
        JobsJobIdApplicationsPostResponseDto dto = new JobsJobIdApplicationsPostResponseDto();
        dto.setId(jobApplication.getId());
        dto.setStatus(jobApplication.getStatus());
        dto.setApplicationDateTime(jobApplication.getApplicationDateTime());
        dto.setNotes(jobApplication.getNotes());
        dto.setApplicant(userMapper.toSafeUserDto(jobApplication.getUser()));
        dto.setJob(jobMapper.toShortJobDto(jobApplication.getJob()));
        return dto;
    }

    private JobsJobIdApplicationsGetResponseDto toJobsJobIdApplicationsGetResponseDto(JobApplication jobApplication) {
        JobsJobIdApplicationsGetResponseDto dto = new JobsJobIdApplicationsGetResponseDto();
        dto.setId(jobApplication.getId());
        dto.setStatus(jobApplication.getStatus());
        dto.setApplicationDateTime(jobApplication.getApplicationDateTime());
        dto.setNotes(jobApplication.getNotes());
        dto.setApplicant(userMapper.toSafeUserDto(jobApplication.getUser()));
        return dto;
    }


    public List<JobsJobIdApplicationsGetResponseDto> toJobsJobIdApplicationsGetResponseDtoList(List<JobApplication> jobApplications) {
        return jobApplications.stream().map(this::toJobsJobIdApplicationsGetResponseDto).toList();
    }

    public JobApplicationsApplicationIdPatchResponseDto toJobApplicationsApplicationIdPatchResponseDto(JobApplication jobApplication){
        JobApplicationsApplicationIdPatchResponseDto dto = new JobApplicationsApplicationIdPatchResponseDto();
        dto.setId(jobApplication.getId());
        dto.setStatus(jobApplication.getStatus());
        dto.setApplicationDateTime(jobApplication.getApplicationDateTime());
        dto.setNotes(jobApplication.getNotes());
        dto.setJob(jobMapper.toShortJobDto(jobApplication.getJob()));
        dto.setApplicant(userMapper.toSafeUserDto(jobApplication.getUser()));
        if (jobApplication.getAssignment() != null) {
            dto.setAssignment(jobAssignmentMapper.toShortJobAssignmentDto(jobApplication.getAssignment()));
        }

        return dto;
    }


}

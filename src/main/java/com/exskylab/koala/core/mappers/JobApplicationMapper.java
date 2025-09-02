package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.jobApplication.response.JobsJobIdApplicationsResponseDto;
import com.exskylab.koala.entities.JobApplication;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {

    private final UserMapper userMapper;
    private final JobMapper jobMapper;

    public JobApplicationMapper(UserMapper userMapper, JobMapper jobMapper) {
        this.userMapper = userMapper;
        this.jobMapper = jobMapper;
    }

    public JobsJobIdApplicationsResponseDto toJobsJobIdApplicationsResponseDto(JobApplication jobApplication) {

        JobsJobIdApplicationsResponseDto dto = new JobsJobIdApplicationsResponseDto();
        dto.setId(jobApplication.getId());
        dto.setStatus(jobApplication.getStatus());
        dto.setApplicationDateTime(jobApplication.getApplicationDateTime());
        dto.setNotes(jobApplication.getNotes());
        dto.setApplicant(userMapper.toSafeUserDto(jobApplication.getUser()));
        dto.setJob(jobMapper.toShortJobDto(jobApplication.getJob()));
        return dto;
    }


}

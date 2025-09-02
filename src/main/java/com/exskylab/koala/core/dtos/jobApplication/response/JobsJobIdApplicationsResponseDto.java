package com.exskylab.koala.core.dtos.jobApplication.response;

import com.exskylab.koala.core.dtos.job.response.ShortJobDto;
import com.exskylab.koala.core.dtos.user.response.SafeUserDto;
import com.exskylab.koala.entities.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobsJobIdApplicationsResponseDto {

    private UUID id;

    private ApplicationStatus status;

    private LocalDateTime applicationDateTime;

    private String notes;

    private SafeUserDto applicant;

    private ShortJobDto job;


}

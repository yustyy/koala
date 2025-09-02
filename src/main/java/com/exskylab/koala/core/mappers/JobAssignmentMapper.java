package com.exskylab.koala.core.mappers;

import com.exskylab.koala.core.dtos.jobAssignment.response.ShortJobAssignmentDto;
import com.exskylab.koala.entities.JobAssignment;
import org.springframework.stereotype.Component;

@Component
public class JobAssignmentMapper {

    public ShortJobAssignmentDto toShortJobAssignmentDto(JobAssignment jobAssignment){
        ShortJobAssignmentDto dto = new ShortJobAssignmentDto();
        dto.setId(jobAssignment.getId());
        dto.setStatus(jobAssignment.getStatus());
        dto.setAssignmentDateTime(jobAssignment.getAssignmentDateTime());

        return dto;
    }



}

package com.exskylab.koala.core.dtos.jobApplication.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NotNull
public class JobsJobIdApplicationsRequestDto {

    private String notes;

}

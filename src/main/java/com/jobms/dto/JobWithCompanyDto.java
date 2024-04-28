package com.jobms.dto;

import com.jobms.domain.Job;
import lombok.Data;

@Data
public class JobWithCompanyDto {
    private Job job;
    private Company company;
}

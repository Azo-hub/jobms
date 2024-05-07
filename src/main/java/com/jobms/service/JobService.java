package com.jobms.service;



import com.jobms.domain.Job;
import com.jobms.dto.JobDto;

import java.util.List;

public interface JobService {
    List<JobDto> findAll();
    void createJob(Job job);

    Job getJobById(Long id);

    boolean deleteJob(Long id);

    Job updateJob(Long id, Job job);
}

package com.jobms.service.Impl;


import com.jobms.domain.Job;
import com.jobms.repository.JobRepository;
import com.jobms.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;


    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteJob(Long id) {
        try{
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public Job updateJob(Long id, Job job) {
        Optional<Job> dbJob = jobRepository.findById(id);
        dbJob.ifPresent(j -> {
                    j.setTitle(job.getTitle());
                    j.setDescription(job.getDescription());
                    j.setMinSalary(job.getMinSalary());
                    j.setMaxSalary(job.getMaxSalary());
                    j.setLocation(job.getLocation());

                    jobRepository.save(j);
                }
        );

        return dbJob.orElse(null);

    }
}

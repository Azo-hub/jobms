package com.jobms.service.Impl;


import com.jobms.domain.Job;
import com.jobms.dto.Company;
import com.jobms.dto.JobWithCompanyDto;
import com.jobms.repository.JobRepository;
import com.jobms.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<JobWithCompanyDto> findAll() {
        List<Job> jobs = jobRepository.findAll();
        return convertJobs(jobs);
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


    private List<JobWithCompanyDto> convertJobs(List<Job> jobs) {
        List<JobWithCompanyDto> jobWithCompanyDtos = new ArrayList<>();

        jobs.forEach(job -> {
            jobWithCompanyDtos.add(convertJob(job));
        });
        return jobWithCompanyDtos;
    }

    private JobWithCompanyDto convertJob(Job job) {
        Company company = restTemplate.getForObject("http://COMPANYMS:8081/companies/"+job.getCompanyId(), Company.class);
        JobWithCompanyDto jobWithCompanyDto = mapToJobWithCompanyDto(job);
        jobWithCompanyDto.setCompany(company);

        return jobWithCompanyDto;
    }

    private JobWithCompanyDto mapToJobWithCompanyDto(Job job) {
        JobWithCompanyDto jobWithCompanyDto = new JobWithCompanyDto();
        jobWithCompanyDto.setId(job.getId());
        jobWithCompanyDto.setTitle(job.getTitle());
        jobWithCompanyDto.setLocation(job.getLocation());
        jobWithCompanyDto.setDescription(job.getDescription());
        jobWithCompanyDto.setMinSalary(job.getMinSalary());
        jobWithCompanyDto.setMaxSalary(job.getMaxSalary());

        return jobWithCompanyDto;
    }


}

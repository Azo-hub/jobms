package com.jobms.service.Impl;


import com.jobms.domain.Job;
import com.jobms.dto.Company;
import com.jobms.dto.JobDto;
import com.jobms.dto.Review;
import com.jobms.repository.JobRepository;
import com.jobms.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    public List<JobDto> findAll() {
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


    private List<JobDto> convertJobs(List<Job> jobs) {
        List<JobDto> jobDtos = new ArrayList<>();

        jobs.forEach(job -> {
            jobDtos.add(convertJob(job));
        });
        return jobDtos;
    }

    private JobDto convertJob(Job job) {
        Company company = restTemplate.getForObject("http://COMPANYMS:8081/companies/"+job.getCompanyId(), Company.class);
        ResponseEntity<List<Review>> reviewResponse =
                restTemplate.exchange("http://REVIEWMS:8083/reviews?companyId="+job.getCompanyId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {
                });
        List<Review> reviews = reviewResponse.getBody();

        JobDto jobDto = mapToJobDto(job);
        jobDto.setCompany(company);
        jobDto.setReviews(reviews);

        return jobDto;
    }

    private JobDto mapToJobDto(Job job) {
        JobDto jobDto = new JobDto();
        jobDto.setId(job.getId());
        jobDto.setTitle(job.getTitle());
        jobDto.setLocation(job.getLocation());
        jobDto.setDescription(job.getDescription());
        jobDto.setMinSalary(job.getMinSalary());
        jobDto.setMaxSalary(job.getMaxSalary());

        return jobDto;
    }


}

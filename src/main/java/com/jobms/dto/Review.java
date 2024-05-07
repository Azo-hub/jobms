package com.jobms.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Review {
    private long id;
    private String title;
    private String description;
    private double rating;


}

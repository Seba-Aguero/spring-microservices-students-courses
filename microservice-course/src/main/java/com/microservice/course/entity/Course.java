package com.microservice.course.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 100, message = "Course name must be between 3 and 100 characters")
    private String name;
    
    @NotBlank(message = "Teacher name is required")
    @Size(min = 3, max = 100, message = "Teacher name must be between 3 and 100 characters")
    private String teacher;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}


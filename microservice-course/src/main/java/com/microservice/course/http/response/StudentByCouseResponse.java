package com.microservice.course.http.response;

import com.microservice.course.controller.dto.StudentDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentByCouseResponse {

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotBlank(message = "Teacher name is required")
    private String teacher;

    @NotNull(message = "Student list cannot be null")
    private List<@Valid StudentDTO> studentDTOList;
}

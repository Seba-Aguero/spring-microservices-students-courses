package com.microservice.course.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEvent implements Serializable {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private Long courseId;
    private String eventType; // CREATED, UPDATED, DELETED
    private LocalDateTime timestamp;
}
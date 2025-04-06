package com.microservice.course.messaging.consumer;

import com.microservice.course.controller.dto.StudentDTO;
import com.microservice.course.messaging.event.StudentEvent;
import com.microservice.course.service.StudentCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentEventConsumer {

    private final StudentCacheService studentCacheService;

    @RabbitListener(queues = "student-events-queue")
    public void handleStudentEvent(StudentEvent event) {
        log.info("Received student event: {}", event);

        StudentDTO studentDTO = mapToStudentDTO(event);

        switch (event.getEventType()) {
            case "CREATED":
                studentCacheService.addOrUpdateStudent(studentDTO, event.getCourseId());
                break;

            case "UPDATED":
                studentCacheService.addOrUpdateStudent(studentDTO, event.getCourseId());
                break;

            case "DELETED":
                studentCacheService.removeStudent(event.getId(), event.getCourseId());
                break;

            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
    }

    private StudentDTO mapToStudentDTO(StudentEvent event) {
        return StudentDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .lastName(event.getLastName())
                .email(event.getEmail())
                .courseId(event.getCourseId())
                .build();
    }
}
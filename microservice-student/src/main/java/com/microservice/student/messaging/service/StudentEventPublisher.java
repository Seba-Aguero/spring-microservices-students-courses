package com.microservice.student.messaging.service;

import com.microservice.student.entity.Student;
import com.microservice.student.messaging.config.RabbitMQConfig;
import com.microservice.student.messaging.event.StudentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishStudentCreated(Student student) {
        publishStudentEvent(student, "CREATED");
    }

    public void publishStudentUpdated(Student student) {
        publishStudentEvent(student, "UPDATED");
    }

    public void publishStudentDeleted(Student student) {
        publishStudentEvent(student, "DELETED");
    }

    private void publishStudentEvent(Student student, String eventType) {
        StudentEvent event = StudentEvent.builder()
                .id(student.getId())
                .name(student.getName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .courseId(student.getCourseId())
                .eventType(eventType)
                .timestamp(LocalDateTime.now())
                .build();

        log.info("Publishing student event: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                "student.events." + eventType.toLowerCase(),
                event
        );
    }
}


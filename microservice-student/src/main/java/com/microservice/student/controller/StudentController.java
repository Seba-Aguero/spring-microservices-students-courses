package com.microservice.student.controller;

import com.microservice.student.controller.dto.StudentUpdateDTO;
import com.microservice.student.entity.Student;
import com.microservice.student.exception.ResourceNotFoundException;
import com.microservice.student.service.IStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@Tag(name = "Student Controller", description = "API for student management")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @Operation(summary = "Create a new student", description = "Creates a new student in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student data provided")
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStudent(@Valid @RequestBody Student student) {
        studentService.save(student);
    }

    @Operation(summary = "Get all students", description = "Returns a list of all students")
    @ApiResponse(responseCode = "200", description = "List of students retrieved successfully")
    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @Operation(summary = "Find student by ID", description = "Returns a student based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/search/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @Operation(summary = "Find students by course ID", description = "Returns all students enrolled in a specific course")
    @ApiResponse(responseCode = "200", description = "List of students retrieved successfully")
    @GetMapping("/search-by-course/{courseId}")
    public ResponseEntity<?> findByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.findAllByCourseId(courseId));
    }

    @Operation(summary = "Update a student", description = "Updates an existing student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid student data provided"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentUpdateDTO studentDTO) {
        try {
            Student updatedStudent = studentService.update(id, studentDTO);
            return ResponseEntity.ok(updatedStudent);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a student", description = "Deletes an existing student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            Student existingStudent = studentService.findById(id);
            if (existingStudent == null) {
                return ResponseEntity.notFound().build();
            }

            studentService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


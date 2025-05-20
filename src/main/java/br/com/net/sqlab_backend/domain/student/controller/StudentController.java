package br.com.net.sqlab_backend.domain.student.controller;

import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.student.models.Student;
import br.com.net.sqlab_backend.domain.student.services.StudentService;
import br.com.net.sqlab_backend.domain.student.validation.StudentValidation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students") // Padrão em inglês
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/create") // Padrão em inglês
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        List<String> validationErrors = StudentValidation.validateStudent(student);

        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        if (studentService.studentExistsByEmail(student.getEmail())) {
            return new ResponseEntity<>("A student with this email already exists.", HttpStatus.CONFLICT);
        }

        if (studentService.studentExistsByRegistrationNumber(student.getRegistrationNumber())) {
            return new ResponseEntity<>("A student with this registration number already exists.", HttpStatus.CONFLICT);
        }

        Student savedStudent = studentService.saveStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @GetMapping("/grades")
    public ResponseEntity<?> getGrades(@RequestParam Long id) {
        List<Grade> grades = studentService.getGrades(id);
        
        return ResponseEntity.status(HttpStatus.OK).body(grades);
    }
}

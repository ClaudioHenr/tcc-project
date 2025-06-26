package br.com.net.sqlab_backend.domain.student.controller;

import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.student.dto.StudentRankingDTO; // Import the new DTO
import br.com.net.sqlab_backend.domain.student.models.Student;
import br.com.net.sqlab_backend.domain.student.services.StudentService;
import br.com.net.sqlab_backend.domain.student.validation.StudentValidation;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
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

    /**
     * Endpoint to get student ranking for a given grade and optional list.
     * Accessible by professors.
     * @param gradeId The ID of the grade to filter by.
     * @param listId Optional. The ID of the list to filter by.
     * @return A list of StudentRankingDTO.
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<StudentRankingDTO>> getStudentRanking(
        @RequestParam Long gradeId,
        @RequestParam(required = false) Long listId
    ) {
        List<StudentRankingDTO> ranking = studentService.getStudentRanking(gradeId, listId);
        return ResponseEntity.ok(ranking);
    }
    
    @PostMapping("/grades/register") // Este endpoint
    public ResponseEntity<?> registerStudentInGrade(
        @RequestParam Long id, // Mapeia para o 'id' da URL
        @RequestParam String codGrade // Mapeia para o 'codGrade' da URL
    ) {
        try {
            // Chamar o serviço com os parâmetros corretos
            studentService.registerInGradeByCod(id, codGrade);
            return ResponseEntity.ok().body("Aluno registrado na turma com sucesso!");
        } catch (IllegalArgumentException e) {
            // Capturar a exceção de negócio para retornar 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            // Capturar caso o aluno com o ID não seja encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Capturar outras exceções inesperadas
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao registrar aluno na turma.");
        }
    }
}
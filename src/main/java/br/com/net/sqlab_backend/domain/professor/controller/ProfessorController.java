package br.com.net.sqlab_backend.domain.professor.controller;

import br.com.net.sqlab_backend.domain.grade.dto.ResponseGetGradeDTO;
import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.professor.models.Professor;
import br.com.net.sqlab_backend.domain.professor.services.ProfessorService;
import br.com.net.sqlab_backend.domain.professor.validation.ProfessorValidation;
import jakarta.annotation.security.PermitAll;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/professors")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping("/create")
    @PermitAll 
    public ResponseEntity<?> createProfessor(@RequestBody Professor professor) {
        List<String> validationErrors = ProfessorValidation.validateProfessor(professor);

        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        if (professorService.professorExistsByEmail(professor.getEmail())) {
            return new ResponseEntity<>("Professor com este email já existe.", HttpStatus.CONFLICT);
        }

        Professor savedProfessor = professorService.saveProfessor(professor);
        return new ResponseEntity<>(savedProfessor, HttpStatus.CREATED);
    }    


    @GetMapping("/grades")
    public ResponseEntity<?> getGrades(@RequestParam Long id) {
        List<Grade> grades = professorService.getGrades(id);
        List<ResponseGetGradeDTO> dtos = new ArrayList<>();
        for (Grade grade : grades) {
            dtos.add(new ResponseGetGradeDTO(grade.getId(), grade.getName(), grade.getSubject(), grade.getCod()));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(dtos);
    }

}

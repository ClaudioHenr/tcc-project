package br.com.net.sqlab_backend.domain.professor.controller;

import br.com.net.sqlab_backend.domain.professor.models.Professor;
import br.com.net.sqlab_backend.domain.professor.services.ProfessorService;
import br.com.net.sqlab_backend.domain.professor.validation.ProfessorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professors") // Alterado para "professors" em inglês
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping("/create") // Alterado para "create" em inglês
    public ResponseEntity<?> createProfessor(@RequestBody Professor professor) {
        List<String> validationErrors = ProfessorValidation.validateProfessor(professor);

        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        if (professorService.professorExistsByEmail(professor.getEmail())) {
            return new ResponseEntity<>("A professor with this email already exists.", HttpStatus.CONFLICT); // HTTP 409 - email already exists
        }

        Professor savedProfessor = professorService.saveProfessor(professor);

        return new ResponseEntity<>(savedProfessor, HttpStatus.CREATED);
    }
}

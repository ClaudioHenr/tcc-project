package br.com.net.sqlab_backend.domain.grade.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.net.sqlab_backend.domain.grade.services.GradeService;
import br.com.net.sqlab_backend.domain.models.ListExercise;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/grades")
public class GradeController {
    
    private GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/listexercises")
    public ResponseEntity<?> getListExercises(@RequestParam Long id) {
        List<ListExercise> listExercises = gradeService.getListExercisesByGradeId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(listExercises);
    }
    
}

package br.com.net.sqlab_backend.domain.grade.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.grade.services.GradeService;
import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/grades")
public class GradeController {
    
    private GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGrade(@RequestBody Grade entity) {
        Grade gradeCreated = gradeService.save(entity);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(gradeCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGrade(@PathVariable Long id) {
        Grade grade = gradeService.get(id);
        return ResponseEntity.status(HttpStatus.OK).body(grade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrade(@PathVariable Long id, @RequestBody Grade update) {
        Grade grade = gradeService.update(id, update);
        return ResponseEntity.status(HttpStatus.OK).body(grade);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrade(@PathVariable Long id) {
        gradeService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Turma excluida com sucesso");
    }
    

    @GetMapping("/listexercises")
    public ResponseEntity<?> getListExercises(@RequestParam Long id) {
        List<ListExercise> listExercises = gradeService.getListExercisesByGradeId(id);
        return ResponseEntity.status(HttpStatus.OK).body(listExercises);
    }
    
}

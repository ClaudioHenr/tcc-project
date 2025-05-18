package br.com.net.sqlab_backend.domain.exercises.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.net.sqlab_backend.domain.exercises.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.ResponseExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.services.ExerciseService;
import br.com.net.sqlab_backend.domain.exercises.services.SolveExerciseService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//@CrossOrigin
@RestController
@RequestMapping("api/exercise")
public class ExercisesController {

    private SolveExerciseService solveExerciseService;

    private ExerciseService exerciseService;

    public ExercisesController(SolveExerciseService solveExerciseService, ExerciseService exerciseService) {
        this.solveExerciseService = solveExerciseService;
        this.exerciseService = exerciseService;
    }

    @PostMapping("/solve")
    public ResponseEntity<ResponseExerciseDTO> solveExercise(@RequestBody QueryExerciseDTO queryDTO) {
        System.out.println("Chegou até solveExercise");
        ResponseExerciseDTO result = solveExerciseService.handleSolveExercise(queryDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getMethodName(@PathVariable Long id) {
        Exercise exercise = exerciseService.getById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(exercise);
    }
    
    @GetMapping("/list")
    public ResponseEntity<?> getAllExercisesByListExerciseId(@RequestParam Long id) {
        List<Exercise> exercises = exerciseService.getByListExerciseId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(exercises);
    }
    


}

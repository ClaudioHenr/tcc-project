package br.com.net.sqlab_backend.domain.exercises.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.net.sqlab_backend.domain.exercises.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.exercise.RequestCreateExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.exercise.ResponseCreateExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.exercise.ResponseGetExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.exercise.ResponseSolveExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.exercise.ResponseUpdateExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.services.ExerciseService;
import br.com.net.sqlab_backend.domain.exercises.services.SolveExerciseService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
    public ResponseEntity<ResponseSolveExerciseDTO> solveExercise(@RequestBody QueryExerciseDTO queryDTO) {
        System.out.println("Chegou até solveExercise");
        ResponseSolveExerciseDTO result = solveExerciseService.handleSolveExercise(queryDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getExercise(@PathVariable Long id) {
        Exercise exercise = exerciseService.getById(id);
        ResponseGetExerciseDTO dto = new ResponseGetExerciseDTO(id, exercise.getDescription(), exercise.getDialect(), exercise.getType(), exercise.getTableName());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createExercise(@RequestBody RequestCreateExerciseDTO request) {
        Exercise exerciseCreated = exerciseService.save(request);
        ResponseCreateExerciseDTO dto = new ResponseCreateExerciseDTO(exerciseCreated.getId(), 
                                                            exerciseCreated.getDescription(), 
                                                            exerciseCreated.getDialect(), 
                                                            exerciseCreated.getType(), 
                                                            exerciseCreated.getSort(), 
                                                            exerciseCreated.getIsPublic(), 
                                                            exerciseCreated.getTableName(), 
                                                            exerciseCreated.getProfessor().getId(), 
                                                            exerciseCreated.getListExercise().getId()
                                                        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(@PathVariable Long id, @RequestBody Exercise update) {
        Exercise exercise = exerciseService.update(id, update);
        ResponseUpdateExerciseDTO dto = new ResponseUpdateExerciseDTO(exercise.getId(), exercise.getDescription(), exercise.getDialect(), exercise.getType(), exercise.getSort(), exercise.getIsPublic(), exercise.getTableName());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id) {
        exerciseService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Exercício excluido com sucesso");
    }
    
    @GetMapping("/list")
    public ResponseEntity<?> getAllExercisesByListExerciseId(@RequestParam Long id) {
        List<Exercise> exercises = exerciseService.getByListExerciseId(id);
        List<ResponseGetExerciseDTO> exercisesDTO = new ArrayList<>();
        for (Exercise exercise : exercises) {
            exercisesDTO.add(new ResponseGetExerciseDTO(exercise.getId(), exercise.getDescription(), exercise.getDialect(), exercise.getType(), exercise.getTableName()));
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(exercisesDTO);
    }
    


}

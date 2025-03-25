package br.com.net.sqlab_backend.domain_h2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.net.sqlab_backend.domain_h2.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain_h2.exercises.services.ExerciseService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin
@RestController
@RequestMapping("api/exercices")
public class ExercisesController {

    @Autowired
    private ExerciseService exerciseService;
    
    @PostMapping("/test")
    public String testSolveExercise(@RequestBody QueryExerciseDTO queryDTO) {
        String result = exerciseService.getDataExerciseTest(queryDTO);
        return result;
    }

    @PostMapping("/test/create")
    public String postMethodName(@RequestBody QueryExerciseDTO queryDTO) {
        String result = exerciseService.createTableExerciseTest(queryDTO);
        return result;
    }
    
    
    @PostMapping("/test/insert")
    public String testSolveInsertExercise(@RequestBody QueryExerciseDTO queryDTO) {
        String result = exerciseService.insertDataExerciseTest(queryDTO);        
        return result;
    }
    
    @PostMapping("/test/delete")
    public String testSolveDeleteExercise(@RequestBody QueryExerciseDTO queryDTO) {
        String result = exerciseService.deleteDataExerciseTest(queryDTO);        
        return result;
    }
    
}

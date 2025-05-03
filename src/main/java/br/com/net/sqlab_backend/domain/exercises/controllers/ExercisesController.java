package br.com.net.sqlab_backend.domain.exercises.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.net.sqlab_backend.domain.exercises.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.ResponseExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.services.SolveExerciseService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

//@CrossOrigin
@RestController
@RequestMapping("api/exercise")
public class ExercisesController {

    @Autowired
    private SolveExerciseService solveExerciseService;

    @PostMapping("/solve")
    public ResponseExerciseDTO solveExercise(@RequestBody QueryExerciseDTO queryDTO) {
        ResponseExerciseDTO result = solveExerciseService.handleSolveExercise(queryDTO);
        return result;
    }
    
}

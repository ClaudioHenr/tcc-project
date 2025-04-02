package br.com.net.sqlab_backend.domain_h2.jdbc_template.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.net.sqlab_backend.domain_h2.jdbc_template.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain_h2.jdbc_template.services.ResolveExercise;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("api/exercices/jdbctemplate")
public class ExercisesControllerJdbcTemplate {

    @Autowired
    private ResolveExercise resolveExerciseJdbcTemplate;
    
    @PostMapping("/test")
    public String testSolveSelectExercise(@RequestBody QueryExerciseDTO queryDTO) {
        String result = resolveExerciseJdbcTemplate.solveExerciseSelect(queryDTO.exerciseId(), queryDTO);
        return result;
    }

    // @PostMapping("/test/create")
    // public String testSolveCreateExercise(@RequestBody QueryExerciseDTO queryDTO) {
    //     String result = resolveExerciseJdbcTemplate.createTableExerciseTest(queryDTO.exerciseId(), queryDTO);
    //     return result;
    // }
    
    
    @PostMapping("/test/insert")
    public String testSolveInsertExercise(@RequestBody QueryExerciseDTO queryDTO) {
        String result = resolveExerciseJdbcTemplate.solveExerciseInsert(queryDTO.exerciseId(), queryDTO);        
        return result;
    }
    
}

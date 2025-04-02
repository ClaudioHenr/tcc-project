package br.com.net.sqlab_backend.domain_h2.jdbc_template.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain_h2.jdbc_template.create.ExampleExerciseJdbcTemplate;
import br.com.net.sqlab_backend.domain_h2.jdbc_template.dto.QueryExerciseDTO;

@Service
public class ResolveExercise {
    
    @Autowired
    private ExampleExerciseJdbcTemplate solveExercise;

    public String solveExerciseSelect(Long exerciseId, QueryExerciseDTO queryDTO) {
        // Criar tabela
        solveExercise.executeSqlFile(queryDTO.exerciseId());

        // Executar query
        
        return "";
    }

    public String solveExerciseInsert(Long exerciseId, QueryExerciseDTO queryDTO) {
        solveExercise.executeSqlFile(queryDTO.exerciseId());

        return "";
    }

}

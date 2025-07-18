package br.com.net.sqlab_backend.domain.exercises.dto.exercise;

import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.exercises.enums.ExerciseType;

public record ResponseGetExerciseDTO(

    Long id,

    String title,
    
    String description,

    Dialect dialect,

    ExerciseType type,

    String tableName

) {
   


}

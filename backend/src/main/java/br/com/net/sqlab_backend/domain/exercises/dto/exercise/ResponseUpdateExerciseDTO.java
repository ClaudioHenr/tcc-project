package br.com.net.sqlab_backend.domain.exercises.dto.exercise;

import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.exercises.enums.ExerciseType;

public record ResponseUpdateExerciseDTO(

    Long id,
    
    String description,

    Dialect dialect,

    ExerciseType type,

    Boolean sort,

    Boolean isPublic,

    String tableName

) {
   


}

package br.com.net.sqlab_backend.domain.exercises.dto.exercise;

import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.exercises.enums.ExerciseType;

public record ResponseCreateExerciseDTO(

    Long id,

    String description,

    Dialect dialect,

    ExerciseType type,

    boolean sort,

    boolean isPublic,

    String tableName,

    Long professorId,

    Long listId

) {
    
}

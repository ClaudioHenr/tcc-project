package br.com.net.sqlab_backend.domain.exercises.dto.exercise;

import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.exercises.enums.ExerciseType;

public record RequestCreateExerciseDTO(

    String description,

    Dialect dialect,

    ExerciseType type,

    boolean sort,

    boolean isPublic,

    String tableName,

    Long professorId,

    Long listId,

    String answerProfessor

) {
    
}

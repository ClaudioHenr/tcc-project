package br.com.net.sqlab_backend.domain.exercises.dto.exercise;

import java.util.List;
import java.util.Map;

public record ResponseSolveExerciseDTO(

    boolean isCorrect,

    List<Map<String, Object>> resultQuery,

    int rowsAffected

) {}

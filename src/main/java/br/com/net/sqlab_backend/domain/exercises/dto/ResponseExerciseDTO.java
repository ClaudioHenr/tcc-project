package br.com.net.sqlab_backend.domain.exercises.dto;

import java.util.List;
import java.util.Map;

public record ResponseExerciseDTO(

    boolean isCorrect,

    List<Map<String, Object>> resultQuery,

    int rowsAffected

) {}

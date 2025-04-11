package br.com.net.sqlab_backend.domain.exercises.dto;

public record QueryExerciseDTO(

    Long studentId,

    Long exerciseId,

    String query

) { }

package br.com.net.sqlab_backend.domain_h2.dto;

public record QueryExerciseDTO(

    Long exerciseId,

    String query,

    String dialect,

    String sgbd

) {

}

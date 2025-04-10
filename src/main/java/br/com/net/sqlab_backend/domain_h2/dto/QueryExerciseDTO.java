package br.com.net.sqlab_backend.domain_h2.dto;

public record QueryExerciseDTO(

    Long exerciseId,

    String query,

    int type, // 1-select, 2-update ou delete

    String dialect,

    String sgbd

) {

}

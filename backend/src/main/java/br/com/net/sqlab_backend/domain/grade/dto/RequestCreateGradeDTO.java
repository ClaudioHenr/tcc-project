package br.com.net.sqlab_backend.domain.grade.dto;

public record RequestCreateGradeDTO(

    String name,

    String subject,

    String idProfessor

) {

}

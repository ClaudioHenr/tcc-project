package br.com.net.sqlab_backend.domain.exercises.dto;

public record AnswerStudentCreateDTO(

    String answer,

    Boolean isCorrect,

    Long exercise_id,

    Long student_id

) {
    
}

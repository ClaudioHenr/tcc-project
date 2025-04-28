package br.com.net.sqlab_backend.domain.answer.models;

import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table
public class Answer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    @JoinColumn(name = "exercise_id")
    @OneToOne
    private Exercise exercise;

    private int typeExercise;

    public Answer(String answer, Exercise exercise, int typeExercise) {
        this.answer = answer;
        this.exercise = exercise;
        this.typeExercise = typeExercise;
    }

    public Answer(String answer, Exercise exercise) {
        this.answer = answer;
        this.exercise = exercise;
    }

}

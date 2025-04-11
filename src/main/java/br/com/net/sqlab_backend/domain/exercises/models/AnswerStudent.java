package br.com.net.sqlab_backend.domain.exercises.models;

import br.com.net.sqlab_backend.domain.student.models.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "answer_student")
public class AnswerStudent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    private boolean isCorrect;

    @JoinColumn(name = "exercise_id")
    @ManyToOne
    private Exercise exercise;

    @JoinColumn(name = "student_id")
    @ManyToOne
    private Student student;

}

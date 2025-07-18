package br.com.net.sqlab_backend.domain.exercises.models;

import br.com.net.sqlab_backend.domain.professor.models.Professor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Table(name = "answer_professor", uniqueConstraints = { // <--- Adicione esta anotação
	    @UniqueConstraint(columnNames = {"exercise_id"}) // <--- Garanta que exercise_id é único
	})
@Entity
public class AnswerProfessor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String answer;

    @JoinColumn(name = "professor_id")
    @ManyToOne
    private Professor professor;

    @JoinColumn(name = "exercise_id")
    @OneToOne
    private Exercise exercise;

    public AnswerProfessor(String answer) {
        this.answer = answer;
    }

    public AnswerProfessor(String answer, Exercise exercise, Professor professor) {
        this.answer = answer;
        this.exercise = exercise;
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }



}

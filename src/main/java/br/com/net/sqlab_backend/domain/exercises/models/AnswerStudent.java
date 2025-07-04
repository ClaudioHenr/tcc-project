package br.com.net.sqlab_backend.domain.exercises.models;

import br.com.net.sqlab_backend.domain.student.models.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "answer_student")
@NoArgsConstructor
public class AnswerStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String answer;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Novo campo para armazenar o timestamp de criação da resposta
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Método para preencher automaticamente o campo 'createdAt' antes de persistir a entidade
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public AnswerStudent(String answer, boolean isCorrect, Exercise exercise, Student student) {
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.exercise = exercise;
        this.student = student;
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

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
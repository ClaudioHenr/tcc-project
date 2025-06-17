package br.com.net.sqlab_backend.domain.exercises.models;

import br.com.net.sqlab_backend.domain.student.models.Student;
import jakarta.persistence.Column;
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

	@Column(length = 500)
    private String answer;

    private boolean isCorrect;

    @JoinColumn(name = "exercise_id")
    @ManyToOne
    private Exercise exercise;

    @JoinColumn(name = "student_id")
    @ManyToOne
    private Student student;

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
    
    

}

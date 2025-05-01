package br.com.net.sqlab_backend.domain.exercises.models;

import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.models.ListExercise;
import br.com.net.sqlab_backend.domain.professor.models.Professor;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table
public class Exercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Dialect dialect;

    @JoinColumn(name = "professor_id")
    @ManyToOne
    private Professor professor;

    @JoinColumn(name = "list_id")
    @ManyToOne
    private ListExercise listExercise;

    public Exercise(Dialect dialect) {
        this.dialect = dialect;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public ListExercise getListExercise() {
		return listExercise;
	}

	public void setListExercise(ListExercise listExercise) {
		this.listExercise = listExercise;
	}

    
}

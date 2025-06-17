package br.com.net.sqlab_backend.domain.exercises.models;

import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.exercises.enums.ExerciseType;
import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;
import br.com.net.sqlab_backend.domain.professor.models.Professor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Exercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 500)
	private String description;

    @Enumerated(EnumType.STRING)
    private Dialect dialect;

	@Enumerated(EnumType.STRING)
	private ExerciseType type;

	@Column
	private Boolean sort;

	@Column
	private Boolean isPublic;

	@Column
	private String tableName;

	// @OneToMany
	// private Set<AnswerProfessor> answers = new HashSet<>();
	// @OneToOne(mappedBy = "exercise")
	// private AnswerProfessor answer;

    @JoinColumn(name = "professor_id")
    @ManyToOne
    private Professor professor;

    @JoinColumn(name = "list_id")
    @ManyToOne
    private ListExercise listExercise;

	public Exercise(String title, String description, Dialect dialect, ExerciseType type, Boolean sort, Boolean isPublic,
			String tableName, Professor professor, ListExercise listExercise) {
		this.title = title;
		this.description = description;
		this.dialect = dialect;
		this.type = type;
		this.sort = sort;
		this.isPublic = isPublic;
		this.tableName = tableName;
		this.professor = professor;
		this.listExercise = listExercise;
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

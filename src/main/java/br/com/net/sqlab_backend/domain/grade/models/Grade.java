package br.com.net.sqlab_backend.domain.grade.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;
import br.com.net.sqlab_backend.domain.student.models.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "listExercises")
// @EqualsAndHashCode(onlyExplicitlyIncluded = true) // sem esta parada não funciona sa joça - apenas campos com @EqualsAndHashCode.Include vão ser incluidos nos métodos equals() e hashCode()
@Table(name = "grade")
@Entity
public class Grade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String subject;

    @Column(unique = true)
    private String cod;

    @ManyToMany
    @JoinTable(
        name = "grade_list_exercise",
        joinColumns = @JoinColumn(name = "grade_id"),
        inverseJoinColumns = @JoinColumn(name = "list_exercise_id")
    )
    private Set<ListExercise> listExercises = new HashSet<>();   
    
    @JsonIgnore
    @ManyToMany(mappedBy = "grades") 
    private Set<Student> students = new HashSet<>();
    
    
    public Grade(Object object, String name2, String subject2, Object object2, Object object3) {
		// TODO Auto-generated constructor stub
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Grade other = (Grade) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (listExercises == null) {
            if (other.listExercises != null)
                return false;
        } else if (!listExercises.equals(other.listExercises))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        return result;
    }

    
    
    
	public Grade() {
		super();
	}

	public Grade(Long id, String name, String subject, String cod) {
		super();
		this.id = id;
		this.name = name;
		this.subject = subject;
		this.cod = cod;
	}

	
	public Grade(Long id, String name, String subject, String cod, Set<ListExercise> listExercises) {
		super();
		this.id = id;
		this.name = name;
		this.subject = subject;
		this.cod = cod;
		this.listExercises = listExercises;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Set<ListExercise> getListExercises() {
		return listExercises;
	}

	public void setListExercises(Set<ListExercise> listExercises) {
		this.listExercises = listExercises;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

    
    
}

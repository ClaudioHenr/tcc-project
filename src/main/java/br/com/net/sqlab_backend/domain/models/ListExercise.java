package br.com.net.sqlab_backend.domain.models;

import java.util.HashSet;
import java.util.Set;

import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "list_exercise")
@Entity
public class ListExercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @OneToMany(mappedBy = "listExercise")
    private Set<Exercise> exercises = new HashSet<>();

}

package br.com.net.sqlab_backend.domain.exercises.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.net.sqlab_backend.domain.exercises.models.Exercise;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    
    List<Exercise> findAllByListExerciseId(Long id);

    List<Exercise> findAllByProfessorId(Long id);

}

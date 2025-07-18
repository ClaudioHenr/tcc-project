package br.com.net.sqlab_backend.domain.exercises.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.net.sqlab_backend.domain.exercises.models.AnswerProfessor;

@Repository
public interface AnswerProfessorRepository extends JpaRepository<AnswerProfessor, Long> {
 
    Optional<AnswerProfessor> findByExerciseId(Long exerciseId);

}

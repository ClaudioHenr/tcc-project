package br.com.net.sqlab_backend.domain.answer.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.net.sqlab_backend.domain.answer.models.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    Optional<Answer> findByExerciseId(Long exerciseId);

}

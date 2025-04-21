package br.com.net.sqlab_backend.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.net.sqlab_backend.domain.models.ListExercise;

@Repository
public interface ListExerciseRepository extends JpaRepository<ListExercise, Long> {
    
}

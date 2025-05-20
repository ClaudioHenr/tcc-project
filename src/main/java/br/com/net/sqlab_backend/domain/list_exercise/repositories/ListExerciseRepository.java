package br.com.net.sqlab_backend.domain.list_exercise.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;

@Repository
public interface ListExerciseRepository extends JpaRepository<ListExercise, Long> {
    
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM grade_list_exercise WHERE list_exercise_id = :listId", nativeQuery = true)
    void removeListExerciseAssociationsById(@Param("listId") Long lisId);

}

package br.com.net.sqlab_backend.domain.grade.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.models.ListExercise;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("SELECT g.listExercises FROM Grade g WHERE g.id = :id")
    List<ListExercise> findListExercisesByGradeId(@Param("id") Long id);

    @Query("SELECT p.grades FROM Professor p WHERE p.id = :id")
    List<Grade> findGradesByProfessorId(@Param("id") Long id);
}

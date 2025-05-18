package br.com.net.sqlab_backend.domain.professor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.professor.models.Professor;

@Repository
public interface ProfessorGradeRepository extends JpaRepository<Professor, Long> {
    
    @Query("SELECT p.grades FROM Professor p WHERE p.id = :id")
    List<Grade> findGradesByProfessorId(@Param("id") Long id);

}

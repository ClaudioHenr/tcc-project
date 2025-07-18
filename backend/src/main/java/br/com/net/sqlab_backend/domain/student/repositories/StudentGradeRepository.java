package br.com.net.sqlab_backend.domain.student.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.student.models.Student;

public interface StudentGradeRepository extends JpaRepository<Student, Long> {
    
    @Query("SELECT p.grades FROM Student p WHERE p.id = :id")
    List<Grade> findGradesByStudentId(@Param("id") Long id);

}

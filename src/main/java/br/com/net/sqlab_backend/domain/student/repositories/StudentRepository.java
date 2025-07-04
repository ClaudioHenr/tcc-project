package br.com.net.sqlab_backend.domain.student.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.student.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findById(Long id);

    Optional<Student> findByName(String name);

    Optional<Student> findByEmail(String email);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    boolean existsByRegistrationNumber(String registrationNumber);

    // Novo método que busca alunos que estão associados a uma dada Grade,
    // e usa FETCH JOIN para carregar as 'studentAnswers', 'exercise' e 'listExercise'
    // em uma única consulta, melhorando a performance para o cálculo do ranking.
    // O DISTINCT é importante para evitar duplicação de alunos caso tenham múltiplas respostas.
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.studentAnswers sa LEFT JOIN FETCH sa.exercise e LEFT JOIN FETCH e.listExercise le WHERE :grade MEMBER OF s.grades")
    List<Student> findByGradesContaining(Grade grade);
}
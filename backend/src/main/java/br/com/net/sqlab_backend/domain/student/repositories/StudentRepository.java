package br.com.net.sqlab_backend.domain.student.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Alterado: Consulta alunos que pertencem a uma determinada turma (Grade).
     * Removido o FETCH JOIN para as respostas, pois estas serão buscadas separadamente
     * para cada métrica do ranking, conforme solicitado.
     * O DISTINCT garante que cada aluno seja retornado apenas uma vez.
     *
     * @param grade A entidade Grade pela qual filtrar os alunos.
     * @return Uma lista de alunos.
     */
    @Query("SELECT DISTINCT s FROM Student s JOIN s.grades g WHERE g = :grade")
    List<Student> findByGradesContaining(@Param("grade") Grade grade);
}
package br.com.net.sqlab_backend.domain.exercises.repositories;

import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AnswerStudentRepository extends JpaRepository<AnswerStudent, Long> {

    /**
     * Criado: Consulta para obter as últimas tentativas de cada exercício para um dado aluno,
     * filtrando por turma e opcionalmente por lista de exercícios.
     * Esta consulta é otimizada para identificar a resposta mais recente (maior createdAt)
     * para cada exercício que o aluno tentou dentro do escopo definido.
     *
     * @param studentId O ID do aluno.
     * @param gradeId O ID da turma à qual as listas de exercícios devem pertencer.
     * @param listId Opcional. O ID da lista de exercícios para filtrar. Se nulo, considera todas as listas da turma.
     * @return Uma lista de AnswerStudent, representando a última tentativa para cada exercício único.
     */
    @Query("SELECT as FROM AnswerStudent as " +
           "JOIN as.exercise e " +
           "JOIN e.listExercise le " +
           "JOIN le.grades g " +
           "WHERE as.student.id = :studentId " +
           "AND g.id = :gradeId " +
           "AND (:listId IS NULL OR le.id = :listId) " +
           "AND as.createdAt = (SELECT MAX(as2.createdAt) FROM AnswerStudent as2 WHERE as2.student.id = as.student.id AND as2.exercise.id = as.exercise.id)")
    List<AnswerStudent> findLatestAnswersByStudentAndGradeAndList(@Param("studentId") Long studentId,
                                                                  @Param("gradeId") Long gradeId,
                                                                  @Param("listId") Long listId);

    /**
     * Criado: Conta o número total de tentativas (submissões) de um aluno para exercícios
     * dentro de uma turma e, opcionalmente, de uma lista específica.
     *
     * @param studentId O ID do aluno.
     * @param gradeId O ID da turma.
     * @param listId Opcional. O ID da lista de exercícios.
     * @return O número total de tentativas.
     */
    @Query("SELECT COUNT(as) FROM AnswerStudent as " +
           "JOIN as.exercise e " +
           "JOIN e.listExercise le " +
           "JOIN le.grades g " +
           "WHERE as.student.id = :studentId " +
           "AND g.id = :gradeId " +
           "AND (:listId IS NULL OR le.id = :listId)")
    int countTotalAttemptsByStudentAndGradeAndList(@Param("studentId") Long studentId,
                                                   @Param("gradeId") Long gradeId,
                                                   @Param("listId") Long listId);

    /**
     * Criado: Busca todas as respostas de um aluno para exercícios dentro de uma turma
     * e, opcionalmente, de uma lista específica. Usado para o cálculo da pontuação,
     * que precisa de todas as tentativas para determinar as últimas e corretas.
     *
     * @param studentId O ID do aluno.
     * @param gradeId O ID da turma.
     * @param listId Opcional. O ID da lista de exercícios.
     * @return Um Set de AnswerStudent contendo todas as respostas relevantes.
     */
    @Query("SELECT as FROM AnswerStudent as " +
           "JOIN FETCH as.exercise e " + // Adicionado FETCH para carregar o exercício junto
           "JOIN FETCH e.listExercise le " + // Adicionado FETCH para carregar a lista do exercício junto
           "JOIN le.grades g " +
           "WHERE as.student.id = :studentId " +
           "AND g.id = :gradeId " +
           "AND (:listId IS NULL OR le.id = :listId)")
    Set<AnswerStudent> findAllRelevantAnswersByStudentAndGradeAndList(@Param("studentId") Long studentId,
                                                                      @Param("gradeId") Long gradeId,
                                                                      @Param("listId") Long listId);
}
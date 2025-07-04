package br.com.net.sqlab_backend.domain.student.services;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.grade.services.GradeService;
import br.com.net.sqlab_backend.domain.grade.repository.GradeRepository;
import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;
import br.com.net.sqlab_backend.domain.student.dto.StudentRankingDTO;
import br.com.net.sqlab_backend.domain.student.models.Student;
import br.com.net.sqlab_backend.domain.student.repositories.StudentGradeRepository;
import br.com.net.sqlab_backend.domain.student.repositories.StudentRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentGradeRepository studentGradeRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final GradeRepository gradeRepository;
    private final GradeService gradeService;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder,
                          StudentGradeRepository studentGradeRepository,
                          GradeRepository gradeRepository,
                          GradeService gradeService) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentGradeRepository = studentGradeRepository;
        this.gradeRepository = gradeRepository;
        this.gradeService = gradeService;
    }

    public Student getById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            throw new EntityNotFoundException("Estudante não encontrado...");
        }
        return student.get();
    }

    @Transactional
    public Student saveStudent(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    public Optional<Student> getStudentByName(String name) {
        return studentRepository.findByName(name);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public boolean studentExistsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    public boolean studentExistsByRegistrationNumber(String registrationNumber) {
        return studentRepository.existsByRegistrationNumber(registrationNumber);
    }

    public List<Grade> getGrades(Long id) {
        return studentGradeRepository.findGradesByStudentId(id);
    }

    @Transactional
    public void registerInGrade(Long id, String codGrade) {
        Student entity = getById(id);
        Grade found = gradeService.getGradeBycod(codGrade);

        boolean alreadyEnrolled = entity.getGrades().stream()
                .anyMatch(grade -> grade.getId().equals(found.getId()));

        if (alreadyEnrolled) {
            throw new IllegalArgumentException("Aluno já matriculado nesta turma.");
        }

        entity.getGrades().add(found);
        studentRepository.save(entity);
    }

    /**
     * Calcula e retorna o ranking dos alunos para uma turma específica,
     * opcionalmente filtrado por uma lista de exercícios específica.
     * @param gradeId O ID da turma.
     * @param listId Opcional. O ID da lista para filtrar.
     * @return Uma lista de StudentRankingDTO.
     */
    @Transactional(readOnly = true)
    public List<StudentRankingDTO> getStudentRanking(Long gradeId, Long listId) {
        // 1. Validar e buscar a turma
        Optional<Grade> optionalGrade = gradeRepository.findById(gradeId);
        if (optionalGrade.isEmpty()) {
            throw new EntityNotFoundException("Turma não encontrada com o ID: " + gradeId);
        }
        Grade grade = optionalGrade.get();

        // 2. Obter todos os alunos associados a esta turma (com fetch join para respostas e exercícios)
        List<Student> studentsInGrade = studentRepository.findByGradesContaining(grade);

        List<StudentRankingDTO> rankings = new ArrayList<>();

        // 3. Iterar sobre cada aluno para calcular suas métricas de ranking
        for (Student student : studentsInGrade) {
            // Filtrar as respostas do aluno que são relevantes para a turma e, se houver, para a lista específica
            Set<AnswerStudent> relevantAnswers = student.getStudentAnswers().stream()
                .filter(answer -> {
                    Exercise exercise = answer.getExercise();
                    if (exercise == null || exercise.getListExercise() == null) {
                        return false; // Resposta sem exercício ou exercício sem lista associada
                    }
                    ListExercise listExercise = exercise.getListExercise();

                    // Verifica se a lista do exercício pertence à turma
                    boolean belongsToGradeLists = grade.getListExercises().contains(listExercise);

                    // Se listId foi fornecido, filtra também pela lista específica
                    if (listId != null) {
                        return belongsToGradeLists && listExercise.getId().equals(listId);
                    }
                    return belongsToGradeLists;
                })
                .collect(Collectors.toSet());

            // Calcula o número de exercícios corretos (baseado na última tentativa)
            // Criado: Função para calcular exercícios corretos
            int totalCorrectExercises = calculateCorrectExercises(relevantAnswers);

            // Calcula o número total de tentativas (todas as submissões)
            // Criado: Função para calcular o total de tentativas
            int totalAttempts = calculateTotalAttempts(relevantAnswers);

            // Calcula a pontuação (média ponderada)
            // Criado: Função para calcular a pontuação
            double score = calculateScore(relevantAnswers);

            // Adiciona o DTO de ranking para o aluno
            rankings.add(new StudentRankingDTO(
                student.getId(),
                student.getName(),
                totalCorrectExercises, // Agora é o número de exercícios únicos corretos
                totalAttempts,        // Agora é a soma de todas as tentativas
                score
            ));
        }

        // 4. Ordenar a lista de rankings
        rankings.sort(Comparator
            // 1º Exercícios acertados (decrescente)
            .comparingInt(StudentRankingDTO::getTotalCorrectAnswers).reversed()
            // 2º Menor Número de tentativas para o maior (crescente)
            .thenComparingInt(StudentRankingDTO::getTotalExercisesAttempted) // Alterado: Usando getTotalExercisesAttempted do DTO
            // 3º Pontuação (Notas) (decrescente)
            .thenComparingDouble(StudentRankingDTO::getScore).reversed()
        );

        return rankings;
    }

    /**
     * Criado: Função para calcular o número de exercícios que um aluno acertou,
     * considerando apenas a última tentativa para cada exercício.
     * @param answers O conjunto de respostas relevantes do aluno.
     * @return O número de exercícios únicos onde a última tentativa foi correta.
     */
    private int calculateCorrectExercises(Set<AnswerStudent> answers) {
        if (answers == null || answers.isEmpty()) {
            return 0;
        }

        // Mapeia cada exercício para a sua última tentativa (mais recente)
        Map<Long, AnswerStudent> latestAttemptsPerExercise = answers.stream()
            .collect(Collectors.toMap(
                answer -> answer.getExercise().getId(), // Chave: ID do exercício
                answer -> answer, // Valor: a própria AnswerStudent
                // Função de merge: se houver duplicidade de chaves, mantém a AnswerStudent com o createdAt mais recente
                (existing, replacement) -> existing.getCreatedAt().isAfter(replacement.getCreatedAt()) ? existing : replacement
            ));

        // Conta quantos desses últimos tentativas foram de fato corretas
        long correctCount = latestAttemptsPerExercise.values().stream()
            .filter(AnswerStudent::isCorrect)
            .count();

        return (int) correctCount;
    }

    /**
     * Criado: Função para calcular o número total de tentativas (submissões) de um aluno
     * para todos os exercícios relevantes.
     * @param answers O conjunto de respostas relevantes do aluno.
     * @return O número total de tentativas (soma de todas as AnswerStudent).
     */
    private int calculateTotalAttempts(Set<AnswerStudent> answers) {
        return answers != null ? answers.size() : 0;
    }

    /**
     * Criado: Função para calcular a pontuação de um aluno.
     * A pontuação é a porcentagem de exercícios acertados sobre o total de exercícios únicos tentados.
     * @param answers O conjunto de respostas relevantes do aluno.
     * @return A pontuação percentual.
     */
    private double calculateScore(Set<AnswerStudent> answers) {
        if (answers == null || answers.isEmpty()) {
            return 0.0;
        }

        // Mapeia cada exercício para a sua última tentativa (mais recente)
        Map<Long, AnswerStudent> latestAttemptsPerExercise = answers.stream()
            .collect(Collectors.toMap(
                answer -> answer.getExercise().getId(),
                answer -> answer,
                (existing, replacement) -> existing.getCreatedAt().isAfter(replacement.getCreatedAt()) ? existing : replacement
            ));

        // Conta quantos dos últimos tentativas foram de fato corretas
        long correctCount = latestAttemptsPerExercise.values().stream()
            .filter(AnswerStudent::isCorrect)
            .count();

        // O total de exercícios únicos tentados é o número de entradas no mapa de últimas tentativas
        int totalUniqueExercisesAttempted = latestAttemptsPerExercise.size();

        if (totalUniqueExercisesAttempted == 0) {
            return 0.0;
        }

        // Calcula a média ponderada: (exercícios únicos corretos / total de exercícios únicos tentados) * 100
        return ((double) correctCount / totalUniqueExercisesAttempted) * 100.0;
    }
}
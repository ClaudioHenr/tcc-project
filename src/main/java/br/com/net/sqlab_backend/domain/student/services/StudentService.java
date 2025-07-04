package br.com.net.sqlab_backend.domain.student.services;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;
import br.com.net.sqlab_backend.domain.exercises.repositories.AnswerStudentRepository; // Adicionado
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
    private final AnswerStudentRepository answerStudentRepository; // Adicionado

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder,
                          StudentGradeRepository studentGradeRepository,
                          GradeRepository gradeRepository,
                          GradeService gradeService,
                          AnswerStudentRepository answerStudentRepository) { // Adicionado
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentGradeRepository = studentGradeRepository;
        this.gradeRepository = gradeRepository;
        this.gradeService = gradeService;
        this.answerStudentRepository = answerStudentRepository; // Inicializado
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
     * Cada métrica (exercícios corretos, total de tentativas, pontuação)
     * é calculada através de consultas isoladas ao banco de dados, conforme solicitado.
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

        // 2. Obter todos os alunos associados a esta turma (apenas os alunos, sem as respostas ainda)
        List<Student> studentsInGrade = studentRepository.findByGradesContaining(grade);

        List<StudentRankingDTO> rankings = new ArrayList<>();

        // 3. Iterar sobre cada aluno para calcular suas métricas de ranking com consultas isoladas
        for (Student student : studentsInGrade) {
            // Consulta 1: Nome do aluno (já obtido ao carregar studentsInGrade)
            String studentName = student.getName();

            // Consulta 2: Exercícios corretos (baseado na última tentativa)
            // Alterado: Usa o novo método do AnswerStudentRepository para buscar as últimas respostas corretas
            int totalCorrectAnswers = calculateCorrectExercisesIsolated(student.getId(), gradeId, listId);

            // Consulta 3: Número total de tentativas (todas as submissões)
            // Alterado: Usa o novo método do AnswerStudentRepository para contar todas as tentativas
            int totalAttempts = calculateTotalAttemptsIsolated(student.getId(), gradeId, listId);

            // Consulta 4: Pontuação (média ponderada)
            // Alterado: Usa o novo método do AnswerStudentRepository para buscar todas as respostas relevantes
            // e calcula a pontuação em memória.
            double score = calculateScoreIsolated(student.getId(), gradeId, listId);

            // Adiciona o DTO de ranking para o aluno
            rankings.add(new StudentRankingDTO(
                student.getId(),
                studentName,
                totalCorrectAnswers,
                totalAttempts,
                score
            ));
        }

        // 4. Ordenar a lista de rankings
        rankings.sort(Comparator
            // 1º Exercícios acertados (decrescente)
            .comparingInt(StudentRankingDTO::getTotalCorrectAnswers).reversed()
            // 2º Menor Número de tentativas para o maior (crescente)
            .thenComparingInt(StudentRankingDTO::getTotalExercisesAttempted)
            // 3º Pontuação (Notas) (decrescente)
            .thenComparingDouble(StudentRankingDTO::getScore).reversed()
        );

        return rankings;
    }

    /**
     * Criado: Calcula o número de exercícios que um aluno acertou,
     * realizando uma consulta isolada ao banco de dados para obter as últimas tentativas corretas.
     * @param studentId O ID do aluno.
     * @param gradeId O ID da turma.
     * @param listId Opcional. O ID da lista de exercícios.
     * @return O número de exercícios únicos onde a última tentativa foi correta.
     */
    private int calculateCorrectExercisesIsolated(Long studentId, Long gradeId, Long listId) {
        // Busca as últimas respostas para cada exercício do aluno, filtradas por turma e lista.
        List<AnswerStudent> latestAnswers = answerStudentRepository.findLatestAnswersByStudentAndGradeAndList(studentId, gradeId, listId);

        // Conta quantos desses últimas respostas são corretas.
        long correctCount = latestAnswers.stream()
            .filter(AnswerStudent::isCorrect)
            .count();

        return (int) correctCount;
    }

    /**
     * Criado: Calcula o número total de tentativas (submissões) de um aluno,
     * realizando uma consulta isolada ao banco de dados.
     * @param studentId O ID do aluno.
     * @param gradeId O ID da turma.
     * @param listId Opcional. O ID da lista de exercícios.
     * @return O número total de tentativas.
     */
    private int calculateTotalAttemptsIsolated(Long studentId, Long gradeId, Long listId) {
        return answerStudentRepository.countTotalAttemptsByStudentAndGradeAndList(studentId, gradeId, listId);
    }

    /**
     * Criado: Calcula a pontuação de um aluno, buscando todas as respostas relevantes
     * e processando-as em memória para determinar a média ponderada.
     * @param studentId O ID do aluno.
     * @param gradeId O ID da turma.
     * @param listId Opcional. O ID da lista de exercícios.
     * @return A pontuação percentual.
     */
    private double calculateScoreIsolated(Long studentId, Long gradeId, Long listId) {
        // Busca todas as respostas relevantes do aluno para o cálculo detalhado em memória.
        Set<AnswerStudent> allRelevantAnswers = answerStudentRepository.findAllRelevantAnswersByStudentAndGradeAndList(studentId, gradeId, listId);

        if (allRelevantAnswers == null || allRelevantAnswers.isEmpty()) {
            return 0.0;
        }

        // Mapeia cada exercício para a sua última tentativa (mais recente)
        Map<Long, AnswerStudent> latestAttemptsPerExercise = allRelevantAnswers.stream()
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
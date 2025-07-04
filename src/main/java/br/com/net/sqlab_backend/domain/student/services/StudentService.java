package br.com.net.sqlab_backend.domain.student.services;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

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

        // Verifica se o aluno já está associado a esta turma
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
        // Encontra a turma pelo ID
        Optional<Grade> optionalGrade = gradeRepository.findById(gradeId);
        if (optionalGrade.isEmpty()) {
            throw new EntityNotFoundException("Turma não encontrada com o ID: " + gradeId);
        }
        Grade grade = optionalGrade.get();

        // Obtém todos os alunos associados a esta turma.
        // O método findByGradesContaining no StudentRepository carregará as AnswerStudent
        // e seus Exercises e ListExercises relacionados para evitar N+1 queries.
        List<Student> studentsInGrade = studentRepository.findByGradesContaining(grade);

        List<StudentRankingDTO> rankings = new ArrayList<>();

        for (Student student : studentsInGrade) {
            int totalCorrectAnswers = 0;
            // Mapa para armazenar a última tentativa para cada exercício do aluno
            Map<Long, AnswerStudent> latestAnswersPerExercise = new HashMap<>();

            // Filtra as respostas do aluno que pertencem a exercícios associados a listas
            // que por sua vez estão vinculadas à turma selecionada.
            Set<AnswerStudent> relevantAnswers = student.getStudentAnswers().stream()
                .filter(answer -> {
                    ListExercise listExercise = answer.getExercise().getListExercise();
                    // Verifica se a lista do exercício está associada à turma
                    boolean belongsToGradeLists = listExercise != null && grade.getListExercises().contains(listExercise);
                    
                    // Se listId foi fornecido, filtra também pela lista específica
                    if (listId != null) {
                        return belongsToGradeLists && listExercise.getId().equals(listId);
                    }
                    return belongsToGradeLists;
                })
                .collect(Collectors.toSet());

            // Popula o mapa com a última tentativa (mais recente) para cada exercício
            for (AnswerStudent answer : relevantAnswers) {
                Long exerciseId = answer.getExercise().getId();
                // Se não houver uma resposta para este exercício ainda, ou se a atual for mais recente
                if (!latestAnswersPerExercise.containsKey(exerciseId) ||
                    answer.getCreatedAt().isAfter(latestAnswersPerExercise.get(exerciseId).getCreatedAt())) {
                    latestAnswersPerExercise.put(exerciseId, answer);
                }
            }

            // Conta quantos dos exercícios, baseados na última tentativa, foram corretos
            for (AnswerStudent latestAnswer : latestAnswersPerExercise.values()) {
                if (latestAnswer.isCorrect()) {
                    totalCorrectAnswers++;
                }
            }
            
            // O número total de exercícios considerados para o ranking é o tamanho do mapa de últimas tentativas
            int totalExercisesAttempted = latestAnswersPerExercise.size();

            // Calcula a pontuação
            double score = (totalExercisesAttempted > 0) ? ((double) totalCorrectAnswers / totalExercisesAttempted) * 100 : 0;

            // Adiciona o DTO de ranking para o aluno
            rankings.add(new StudentRankingDTO(
                student.getId(),
                student.getName(),
                totalCorrectAnswers,
                totalExercisesAttempted,
                score
            ));
        }

        // Ordena os rankings pela pontuação em ordem decrescente
        rankings.sort(Comparator.comparingDouble(StudentRankingDTO::getScore).reversed());

        return rankings;
    }
}
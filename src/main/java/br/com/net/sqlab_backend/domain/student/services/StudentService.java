package br.com.net.sqlab_backend.domain.student.services;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;
import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.grade.repository.GradeRepository;
import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;
import br.com.net.sqlab_backend.domain.student.dto.StudentRankingDTO; // Import the new DTO
import br.com.net.sqlab_backend.domain.student.models.Student;
import br.com.net.sqlab_backend.domain.student.repositories.StudentGradeRepository;
import br.com.net.sqlab_backend.domain.student.repositories.StudentRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final GradeRepository gradeRepository; // Declare GradeRepository

    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder, StudentGradeRepository studentGradeRepository, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentGradeRepository = studentGradeRepository;
        this.gradeRepository = gradeRepository;
    }

    public Student registerInGradeByCod(Long studentId, String codGrade) { // <-- AQUI!
        // 1. Encontrar o aluno
        Student student = getById(studentId); // Assumindo que getById joga exceção se não encontrar

        // 2. Encontrar a turma pelo código
        Optional<Grade> optionalGrade = gradeRepository.findByCod(codGrade);
        if (optionalGrade.isEmpty()) {
            // Se a turma não for encontrada, lançamos uma exceção de negócio
            throw new IllegalArgumentException("Código de turma inválido ou turma não encontrada.");
        }
        Grade grade = optionalGrade.get();

        // 3. Associar o aluno à turma
        student.getGrades().add(grade);
        grade.getStudents().add(student); // Garanta que Grade também tem um set de students
        studentRepository.save(student); // Salva a atualização no aluno

        return student; // Ou algo que indique sucesso
    }
    public Student getById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            throw new EntityNotFoundException("Estudante não encontrado...");
        }
        return student.get();
    }

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
        List<Grade> grades = new ArrayList<>();
        grades = studentGradeRepository.findGradesByStudentId(id);
        return grades;
    }

    /**
     * Calculates and returns the ranking of students for a specific grade,
     * optionally filtered by a specific list of exercises.
     * @param gradeId The ID of the grade.
     * @param listId Optional. The ID of the list to filter by.
     * @return A list of StudentRankingDTO, sorted by score.
     */
    public List<StudentRankingDTO> getStudentRanking(Long gradeId, Long listId) {
        // Find the grade by ID
        Optional<Grade> optionalGrade = gradeRepository.findById(gradeId); // <-- CORRECTED LINE
        if (optionalGrade.isEmpty()) {
            throw new EntityNotFoundException("Grade not found with ID: " + gradeId);
        }
        Grade grade = optionalGrade.get();

        // Get all students in the specified grade
        // Assuming Grade has a many-to-many relationship with Student, or you can fetch students by grade code
        // For simplicity, let's fetch all students and filter by grades they are associated with.
        // A more efficient way might involve a direct query from a Student-Grade join table.
        List<Student> studentsInGrade = studentRepository.findAll().stream()
            .filter(student -> student.getGrades().contains(grade))
            .collect(Collectors.toList());

        List<StudentRankingDTO> rankings = new ArrayList<>();

        for (Student student : studentsInGrade) {
            int totalCorrectAnswers = 0;
            int totalExercises = 0;

            // Filter answers based on grade and optional list
            Set<AnswerStudent> filteredAnswers = student.getStudentAnswers().stream()
                .filter(answer -> {
                    // Check if the exercise associated with the answer belongs to an exercise list
                    // that is part of the selected grade
                    ListExercise listExercise = answer.getExercise().getListExercise();
                    return listExercise != null && grade.getListExercises().contains(listExercise);
                })
                .collect(Collectors.toSet());
            
            // If listId is provided, further filter by exercises within that specific list
            if (listId != null) {
                filteredAnswers = filteredAnswers.stream()
                    .filter(answer -> answer.getExercise().getListExercise().getId().equals(listId))
                    .collect(Collectors.toSet());
            }

            // Group answers by exercise to count unique exercises attempted
            Map<Long, AnswerStudent> latestAnswerPerExercise = new HashMap<>();
            for (AnswerStudent answer : filteredAnswers) {
                latestAnswerPerExercise.put(answer.getExercise().getId(), answer);
            }

            totalExercises = latestAnswerPerExercise.size();
            totalCorrectAnswers = (int) latestAnswerPerExercise.values().stream()
                                    .filter(AnswerStudent::isCorrect)
                                    .count();

            double score = (totalExercises > 0) ? ((double) totalCorrectAnswers / totalExercises) * 100 : 0;

            rankings.add(new StudentRankingDTO(student.getId(), student.getName(), totalCorrectAnswers, totalExercises, score));
        }

        // Sort the rankings by score in descending order
        rankings.sort((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()));

        return rankings;
    }
}
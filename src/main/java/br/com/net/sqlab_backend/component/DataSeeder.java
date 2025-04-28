package br.com.net.sqlab_backend.component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.net.sqlab_backend.domain.answer.models.Answer;
import br.com.net.sqlab_backend.domain.answer.repositories.AnswerRepository;
import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.repositories.ExerciseRepository;
import br.com.net.sqlab_backend.domain.models.Grade;
import br.com.net.sqlab_backend.domain.models.ListExercise;
import br.com.net.sqlab_backend.domain.repositories.GradeRepository;
import br.com.net.sqlab_backend.domain.repositories.ListExerciseRepository;
import br.com.net.sqlab_backend.domain.student.models.Student;
import br.com.net.sqlab_backend.domain.student.repositories.StudentRepository;

@Component
@Profile({"dev", "test"})
public class DataSeeder implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;
    private final AnswerRepository answerRepository;
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final ListExerciseRepository listExerciseRepository;
    
    public DataSeeder(ExerciseRepository exerciseRepository, AnswerRepository answerRepository,
            StudentRepository studentRepository, GradeRepository gradeRepository, ListExerciseRepository listExerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        this.answerRepository = answerRepository;
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.listExerciseRepository = listExerciseRepository;
    }

    @Override
    public void run(String... args) {
        // Inserção de estudantes
        for (int i = 0; i <= 5; i++) {
            Student student = new Student();
            insertIntoStudent(student);
        };

        // Inserção de exercícios
        Exercise exer01 = exerciseRepository.save(new Exercise(Dialect.POSTGRESQL));
        Exercise exer02 = exerciseRepository.save(new Exercise(Dialect.MYSQL));
        Exercise exer03 = exerciseRepository.save(new Exercise(Dialect.POSTGRESQL));
        Exercise exer04 = exerciseRepository.save(new Exercise(Dialect.MYSQL));
        // Inserção de respostas
        List<String> queries = Arrays.asList("UPDATE users SET name='Rosa' WHERE id=10;", "UPDATE users SET has_driver_license=TRUE WHERE age>29;", "SELECT * FROM users WHERE age > 30;", "SELECT SUM(CASE WHEN has_driver_license THEN 1 ELSE 0 END) FROM users;", "SELECT COUNT(*) AS contagem FROM users;", "SELECT SUM(age) FROM users;", "SELECT * FROM users WHERE age > 30;", "SELECT age, COUNT(*) FROM users GROUP BY age;");
        Answer answer01 = new Answer(queries.get(0), exer01, 2);
        Answer answer02 = new Answer(queries.get(1), exer02, 2);
        Answer answer03 = new Answer(queries.get(2), exer03, 1);
        Answer answer04 = new Answer(queries.get(3), exer04, 1);
        answerRepository.save(answer01);
        answerRepository.save(answer02);
        answerRepository.save(answer03);
        answerRepository.save(answer04);

        // Criação de turma
        Grade grade01 = new Grade();
        Grade grade02 = new Grade();
        gradeRepository.save(grade01);
        gradeRepository.save(grade02);

        // Relacionar aluno com turma
        // Relacionar várias turmas
        List<Grade> listGrades = gradeRepository.findAll();
        connectStudentGrade(1L, listGrades);
        // Relacionar apenas uma
        Iterable<Long> iteId = List.of(1L);
        List<Grade> listGrades02 =  gradeRepository.findAllById(iteId);
        connectStudentGrade(2L, listGrades02);

        // Criar Listas de exercícios e relacionar com turma
        Optional<Grade> existGrade = gradeRepository.findById(1L);
        ListExercise listExercise = new ListExercise(existGrade.get());
        listExerciseRepository.save(listExercise);
        
    }

    public void insertIntoStudent(Student student) {
        studentRepository.save(student);
    }

    public void insertIntoExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public void insertIntoAnswer(Answer answer) {
        answerRepository.save(answer);
    }

    public void connectStudentGrade(Long id, List<Grade> grades) {
        Set<Grade> setGrades = new HashSet<>(grades);
        Optional<Student> stu = studentRepository.findById(id);
        stu.get().setGrade(setGrades);
        studentRepository.save(stu.get());
    }

}

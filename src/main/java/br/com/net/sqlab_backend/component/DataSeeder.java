package br.com.net.sqlab_backend.component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        // Criação de estudantes
        for (int i = 0; i <= 5; i++) {
            Student student = new Student();
            insertIntoStudent(student);
        };

        // Criação de exercícios
        Exercise exer01 = exerciseRepository.save(new Exercise(Dialect.POSTGRESQL));
        Exercise exer02 = exerciseRepository.save(new Exercise(Dialect.MYSQL));
        Exercise exer03 = exerciseRepository.save(new Exercise(Dialect.POSTGRESQL));
        Exercise exer04 = exerciseRepository.save(new Exercise(Dialect.MYSQL));
        Exercise exer05 = exerciseRepository.save(new Exercise(Dialect.MYSQL));
        Exercise exer06 = exerciseRepository.save(new Exercise(Dialect.MYSQL));

        // Criação de lista de exercícios
        Set<Exercise> listExercises = new HashSet<>();
        listExercises.add(exer01);
        listExercises.add(exer02);
        listExercises.add(exer03);
        listExercises.add(exer04);
        ListExercise listExerciseEntity = new ListExercise();
        listExerciseEntity.setExercises(listExercises);
        ListExercise listExerciseCreated = listExerciseRepository.save(listExerciseEntity);

        // Criação de respostas
        List<String> queries = Arrays.asList("UPDATE users SET name='Rosa' WHERE id=10;", "DELETE FROM users WHERE age>29;", "INSERT INTO users (name, age, birth_date, has_driver_license) VALUES ('MAIS UM', 18, '1994-05-15', TRUE);", "SELECT * FROM users WHERE age > 30;", "SELECT SUM(CASE WHEN has_driver_license THEN 1 ELSE 0 END) FROM users;", "SELECT COUNT(*) AS contagem FROM users;", "SELECT SUM(age) FROM users;", "SELECT * FROM users WHERE age > 30;", "SELECT age, COUNT(*) FROM users GROUP BY age;");
        Answer answer01 = new Answer(queries.get(0), exer01, 2);
        Answer answer02 = new Answer(queries.get(1), exer02, 2);
        Answer answer03 = new Answer(queries.get(2), exer03, 2);
        Answer answer04 = new Answer(queries.get(3), exer04, 1);
        Answer answer05 = new Answer(queries.get(4), exer05, 1);
        Answer answer06 = new Answer(queries.get(5), exer06, 1);
        answerRepository.save(answer01);
        answerRepository.save(answer02);
        answerRepository.save(answer03);
        answerRepository.save(answer04);
        answerRepository.save(answer05);
        answerRepository.save(answer06);

        // Criação de turma
        Grade grade01 = new Grade();
        Grade grade02 = new Grade();
        Set<ListExercise> setListExercises = new HashSet<>();
        setListExercises.add(listExerciseCreated);
        grade01.setListExercises(setListExercises);
        gradeRepository.save(grade01);
        gradeRepository.save(grade02);

        // Relacionar aluno com turma
        // Relacionar várias turmas
        connectStudentGrade(1L);
        
    }

    public void createExercises() {

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

    @Transactional
    public void connectStudentGrade(Long id) {
        List<Grade> grades = gradeRepository.findAll();
        Student stu = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        stu.setGrades(new HashSet<Grade>(grades));
        studentRepository.save(stu);
    }

}

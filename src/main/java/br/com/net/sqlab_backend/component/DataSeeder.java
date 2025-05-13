package br.com.net.sqlab_backend.component;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.net.sqlab_backend.domain.answer.models.Answer;
import br.com.net.sqlab_backend.domain.answer.repositories.AnswerRepository;
import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.exercises.enums.ExerciseType;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.repositories.ExerciseRepository;
import br.com.net.sqlab_backend.domain.models.ListExercise;
import br.com.net.sqlab_backend.domain.professor.models.Professor;
import br.com.net.sqlab_backend.domain.repositories.GradeRepository;
import br.com.net.sqlab_backend.domain.repositories.ListExerciseRepository;
import br.com.net.sqlab_backend.domain.student.models.Student;
import br.com.net.sqlab_backend.domain.student.repositories.StudentRepository;

@Component
@Profile({"prod"})
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
        Student student = new Student("test", "email@test.com", "12345", "1234", null, null);
        insertIntoStudent(student);

        // Criação de exercícios
        // Exercise exer01 = exerciseRepository.save(new Exercise(1L, Dialect.POSTGRESQL, ExerciseType.ALTER, false, true));
        // Exercise exer02 = exerciseRepository.save(new Exercise(2L, Dialect.MYSQL, ExerciseType.ALTER, false, true));
        // Exercise exer03 = exerciseRepository.save(new Exercise(3L, Dialect.POSTGRESQL, ExerciseType.ALTER, false, true));
        // Exercise exer04 = exerciseRepository.save(new Exercise(4L, Dialect.MYSQL, ExerciseType.ALTER, false, true));
        // Exercise exer05 = exerciseRepository.save(new Exercise(5L, Dialect.MYSQL, ExerciseType.ALTER, false, true));
        // Exercise exer06 = exerciseRepository.save(new Exercise(6L, Dialect.MYSQL, ExerciseType.ALTER, false, true));
        // exerciseRepository.save(exer01);
        // exerciseRepository.save(exer02);
        // exerciseRepository.save(exer03);
        // exerciseRepository.save(exer04);
        // exerciseRepository.save(exer05);


        // Criação de respostas
        // List<String> queries = Arrays.asList("UPDATE users SET name='Rosa' WHERE id=10;", "DELETE FROM users WHERE age>29;", "INSERT INTO users (name, age, birth_date, has_driver_license) VALUES ('MAIS UM', 18, '1994-05-15', TRUE);", "SELECT * FROM users WHERE age > 30;", "SELECT SUM(CASE WHEN has_driver_license THEN 1 ELSE 0 END) FROM users;", "SELECT COUNT(*) AS contagem FROM users;", "SELECT SUM(age) FROM users;", "SELECT * FROM users WHERE age > 30;", "SELECT age, COUNT(*) FROM users GROUP BY age;");
        // Answer answer01 = new Answer(queries.get(0), exer01);
        // Answer answer02 = new Answer(queries.get(1), exer02);
        // Answer answer03 = new Answer(queries.get(2), exer03);
        // Answer answer04 = new Answer(queries.get(3), exer04);
        // Answer answer05 = new Answer(queries.get(4), exer05);
        // Answer answer06 = new Answer(queries.get(5), exer06);
        // answerRepository.save(answer01);
        // answerRepository.save(answer02);
        // answerRepository.save(answer03);
        // answerRepository.save(answer04);
        // answerRepository.save(answer05);
        // answerRepository.save(answer06);
        
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


}

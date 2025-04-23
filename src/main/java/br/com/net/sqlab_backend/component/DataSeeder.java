package br.com.net.sqlab_backend.component;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.net.sqlab_backend.domain.answer.models.Answer;
import br.com.net.sqlab_backend.domain.answer.repositories.AnswerRepository;
import br.com.net.sqlab_backend.domain.exercises.enums.Dialect;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.repositories.ExerciseRepository;
import br.com.net.sqlab_backend.domain.student.models.Student;
import br.com.net.sqlab_backend.domain.student.repositories.StudentRepository;

@Component
@Profile({"dev", "test"})
public class DataSeeder implements CommandLineRunner {

    private final ExerciseRepository exerciseRepository;
    private final AnswerRepository answerRepository;
    private final StudentRepository studentRepository;
    
    public DataSeeder(ExerciseRepository exerciseRepository, AnswerRepository answerRepository,
            StudentRepository studentRepository) {
        this.exerciseRepository = exerciseRepository;
        this.answerRepository = answerRepository;
        this.studentRepository = studentRepository;
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
        List<String> queries = Arrays.asList("SELECT COUNT(*) AS contagem FROM users;", "SELECT SUM(age) FROM users;", "SELECT * FROM users WHERE age > 30;", "SELECT age, COUNT(*) FROM users GROUP BY age;");
        Answer answer01 = new Answer(queries.get(0), exer01);
        Answer answer02 = new Answer(queries.get(1), exer02);
        Answer answer03 = new Answer(queries.get(2), exer03);
        Answer answer04 = new Answer(queries.get(3), exer04);
        answerRepository.save(answer01);
        answerRepository.save(answer02);
        answerRepository.save(answer03);
        answerRepository.save(answer04);
        
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

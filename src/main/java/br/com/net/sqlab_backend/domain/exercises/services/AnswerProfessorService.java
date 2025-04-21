package br.com.net.sqlab_backend.domain.exercises.services;

import org.springframework.stereotype.Service;

@Service
public class AnswerProfessorService {
    
    public String getAnswerProfessorByExerciseId(Long id) {

        return "SELECT name, age FROM users WHERE age > 30;";
    }

}

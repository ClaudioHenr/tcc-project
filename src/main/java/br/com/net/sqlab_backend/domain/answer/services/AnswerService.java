package br.com.net.sqlab_backend.domain.answer.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.answer.models.Answer;
import br.com.net.sqlab_backend.domain.answer.repositories.AnswerRepository;

@Service
public class AnswerService {
    
    @Autowired
    AnswerRepository answerRepository;

    public Answer getByExerciseId(Long id) {
        Optional<Answer> answer = answerRepository.findByExerciseId(id);
        if (answer.isEmpty()) {
            throw new RuntimeException("Resposta não encontrada para o exercício " + id);
        }
        return answer.get();
    }
}

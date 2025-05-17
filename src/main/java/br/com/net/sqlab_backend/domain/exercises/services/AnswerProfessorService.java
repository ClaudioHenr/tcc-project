package br.com.net.sqlab_backend.domain.exercises.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.exercises.repositories.AnswerProfessorRepository;
import br.com.net.sqlab_backend.domain.models.AnswerProfessor;

@Service
public class AnswerProfessorService {

    @Autowired
    private AnswerProfessorRepository answerProfessorRepository;

    public AnswerProfessor getByExerciseId(Long id) {
        Optional<AnswerProfessor> answer = answerProfessorRepository.findByExerciseId(id);
        if (answer.isEmpty()) {
            throw new EntityNotFoundException("Resposta não encontrada para o exercício " + id);
        }
        return answer.get();
    }

}

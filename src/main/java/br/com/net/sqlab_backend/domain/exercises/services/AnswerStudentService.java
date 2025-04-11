package br.com.net.sqlab_backend.domain.exercises.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.exercises.dto.AnswerStudentCreateDTO;
import br.com.net.sqlab_backend.domain.exercises.mapper.AnswerStudentMapper;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;
import br.com.net.sqlab_backend.domain.exercises.repositories.AnswerStudentRepository;

@Service
public class AnswerStudentService {

    @Autowired
    AnswerStudentRepository answerStudentRepository;

    // @Autowired
    // AnswerStudentMapper answerStudentMapper;

    public AnswerStudent save(AnswerStudentCreateDTO answerStudentCreateDTO) {
        // AnswerStudent answerStudent = answerStudentMapper.toEntity(answerStudentCreateDTO);

        return answerStudentRepository.save(new AnswerStudent());
    }
    
}

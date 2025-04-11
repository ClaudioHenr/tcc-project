package br.com.net.sqlab_backend.domain.exercises.mapper;

import org.mapstruct.Mapper;

import br.com.net.sqlab_backend.domain.exercises.dto.AnswerStudentCreateDTO;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;

@Mapper(componentModel = "spring")
public interface AnswerStudentMapper {
    
    AnswerStudent toEntity(AnswerStudentCreateDTO dto);
    
    AnswerStudentCreateDTO toAnswerStudentCreateDTO(AnswerStudent entity);


}

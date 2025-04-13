package br.com.net.sqlab_backend.domain.exercises.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.net.sqlab_backend.domain.exercises.dto.AnswerStudentCreateDTO;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;

@Mapper(componentModel = "spring") // unmappedTargetPolicy = ReportingPolicy.IGNORE
public interface AnswerStudentMapper {

    // AnswerStudentMapper INSTANCE = Mappers.getMapper(AnswerStudentMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exercise", ignore = true)
    @Mapping(target = "student", ignore = true)
    AnswerStudent toEntity(AnswerStudentCreateDTO dto);
    
    @Mapping(target = "exerciseId", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "isCorrect", ignore = true)
    AnswerStudentCreateDTO toAnswerStudentCreateDTO(AnswerStudent entity);


}

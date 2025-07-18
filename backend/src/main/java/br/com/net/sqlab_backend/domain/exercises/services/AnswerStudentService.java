package br.com.net.sqlab_backend.domain.exercises.services;

import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.exercises.dto.AnswerStudentCreateDTO;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerStudent;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.repositories.AnswerStudentRepository;
import br.com.net.sqlab_backend.domain.student.models.Student;
import br.com.net.sqlab_backend.domain.student.services.StudentService;

@Service
public class AnswerStudentService {

    private AnswerStudentRepository answerStudentRepository;

    private ExerciseService exerciseService;

    private StudentService studentService;

    public AnswerStudentService(AnswerStudentRepository answerStudentRepository, ExerciseService exerciseService, StudentService studentService) {
        this.answerStudentRepository = answerStudentRepository;
        this.exerciseService = exerciseService;
        this.studentService = studentService;
    }

    public AnswerStudent save(AnswerStudentCreateDTO answerStudentCreateDTO) {
        Exercise exercise = exerciseService.getById(answerStudentCreateDTO.exerciseId());
        Student student = studentService.getById(answerStudentCreateDTO.studentId());
        AnswerStudent answerStudent = new AnswerStudent(answerStudentCreateDTO.answer(), false, exercise, student);
        return answerStudentRepository.save(answerStudent);
    }
    
    public AnswerStudent update(AnswerStudent entity) {
        return answerStudentRepository.save(entity);
    }
}

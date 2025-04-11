package br.com.net.sqlab_backend.domain.exercises.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.exercises.dto.AnswerStudentCreateDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.QueryExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.dto.ResponseExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.repositories.ExerciseRepository;

@Service
public class ExerciseService {

    @Autowired
    private AnswerStudentService answerStudentService;

    @Autowired
    private AnswerProfessorService answerProfessorService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    public Exercise getById(Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        if (exercise.isEmpty()) {
            throw new RuntimeException("Exercício não encontrado...");
        }
        return exercise.get();
    }

    public ResponseExerciseDTO handleSolveExercise(QueryExerciseDTO query) {
        // Salvar query em answer_student
        AnswerStudentCreateDTO dto = new AnswerStudentCreateDTO(query.query(), null, query.exerciseId(),query.studentId());
        answerStudentService.save(dto);

        // Recuperar query resposta de answer_professor/resposta pré cadastrada
        String answerProfessor = answerProfessorService.getAnswerProfessorByExerciseId(query.exerciseId());
        System.out.println(answerProfessor);

        // Criar 'ambiente'
        // Connection conn = 

        // Executar as duas queries
        
        // Comparar resultados das duas queries

        // Retornar

        ResponseExerciseDTO res = new ResponseExerciseDTO(false, null);
        return res;
    }

}

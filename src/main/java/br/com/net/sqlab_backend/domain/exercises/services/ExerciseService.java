package br.com.net.sqlab_backend.domain.exercises.services;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.exercises.dto.exercise.RequestCreateExerciseDTO;
import br.com.net.sqlab_backend.domain.exercises.models.AnswerProfessor;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.repositories.ExerciseRepository;
import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;
import br.com.net.sqlab_backend.domain.list_exercise.services.ListExerciseService;
import br.com.net.sqlab_backend.domain.professor.models.Professor;
import br.com.net.sqlab_backend.domain.professor.services.ProfessorService;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final AnswerProfessorService answerProfessorService;
    
    private final ProfessorService professorService;

    private final ListExerciseService listExerciseService;

    public ExerciseService(ExerciseRepository exerciseRepository, AnswerProfessorService answerProfessorService,
            ProfessorService professorService, @Lazy ListExerciseService listExerciseService) {
        this.exerciseRepository = exerciseRepository;
        this.answerProfessorService = answerProfessorService;
        this.professorService = professorService;
        this.listExerciseService = listExerciseService;
    }

    public Exercise getById(Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        if (exercise.isEmpty()) {
            throw new EntityNotFoundException("Exercício não encontrado...");
        }
        return exercise.get();
    }

    @Transactional
    public Exercise save(RequestCreateExerciseDTO exercise) {
        // Buscar Professor
        Optional<Professor> professor = professorService.getProfessorById(exercise.professorId());
        if (professor.isEmpty()) {
            throw new EntityNotFoundException("Professor não encontrado");
        }

        // Buscar Lista
        ListExercise listExercise = listExerciseService.getById(exercise.listId());

        // Salvar exercício
        Exercise newExercise = exerciseRepository.save(new Exercise(exercise.description(), exercise.dialect(), exercise.type(), exercise.sort(), exercise.isPublic(), exercise.tableName(), professor.get(), listExercise));

        // Salvar resposta
        answerProfessorService.save(new AnswerProfessor(exercise.answerProfessor(), newExercise, professor.get()));

        return newExercise;
    }

    public Exercise update(Long id, Exercise update) {
        Optional<Exercise> entity = exerciseRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Exercício não encontrado");
        }
        
        Exercise exercise = entity.get();
        exercise.setDescription(update.getDescription());
        exercise.setDialect(update.getDialect());
        exercise.setType(update.getType());
        exercise.setSort(update.getSort());
        exercise.setIsPublic(update.getIsPublic());
        exercise.setTableName(update.getTableName());

        Exercise exerciseUptaded = exerciseRepository.save(exercise);
        return exerciseUptaded;
    }

    public void delete(Long id) {
        Optional<Exercise> entity = exerciseRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Exercício não encontrado");
        }

        AnswerProfessor answer = answerProfessorService.getByExerciseId(id);
        answerProfessorService.delete(answer.getId());

        exerciseRepository.deleteById(id);
    }

    public List<Exercise> getByListExerciseId(Long id) {
        List<Exercise> exercises = exerciseRepository.findAllByListExerciseId(id);
        return exercises;
    }

}

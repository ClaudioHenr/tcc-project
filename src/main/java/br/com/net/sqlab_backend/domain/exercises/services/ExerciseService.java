package br.com.net.sqlab_backend.domain.exercises.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.repositories.ExerciseRepository;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise getById(Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        if (exercise.isEmpty()) {
            throw new EntityNotFoundException("Exercício não encontrado...");
        }
        return exercise.get();
    }

    public List<Exercise> getByListExerciseId(Long id) {
        List<Exercise> exercises = exerciseRepository.findAllByListExerciseId(id);
        return exercises;
    }

}

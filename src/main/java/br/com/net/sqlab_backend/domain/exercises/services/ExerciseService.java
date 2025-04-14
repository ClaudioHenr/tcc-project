package br.com.net.sqlab_backend.domain.exercises.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.repositories.ExerciseRepository;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    
    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise getById(Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        if (exercise.isEmpty()) {
            throw new RuntimeException("Exercício não encontrado...");
        }
        return exercise.get();
    }

}

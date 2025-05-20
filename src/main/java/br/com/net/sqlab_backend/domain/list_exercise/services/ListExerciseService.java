package br.com.net.sqlab_backend.domain.list_exercise.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.exercises.models.Exercise;
import br.com.net.sqlab_backend.domain.exercises.services.ExerciseService;
import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;
import br.com.net.sqlab_backend.domain.list_exercise.repositories.ListExerciseRepository;

@Service
public class ListExerciseService {
    
    private final ListExerciseRepository listExerciseRepository;

    private final ExerciseService exerciseService;

    public ListExerciseService(ListExerciseRepository listExerciseRepository, ExerciseService exerciseService) {
        this.listExerciseRepository = listExerciseRepository;
        this.exerciseService = exerciseService;
    }

    public ListExercise save(ListExercise listExercise) {
        return listExerciseRepository.save(listExercise);
    }

    public ListExercise getById(Long id) {
        Optional<ListExercise> entity = listExerciseRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Lista de exercício não encontrada");
        }
        return entity.get();
    }

    public ListExercise update(Long id, ListExercise update) {
        Optional<ListExercise> entity = listExerciseRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Lista de exercício não encontrado");
        }

        entity.get().setTitle((update.getTitle()));
        entity.get().setDescription(update.getDescription());
        entity.get().setDescription(update.getDescription());

        return listExerciseRepository.save(entity.get());
    }

    @Transactional
    public void delete(Long id) {
        Optional<ListExercise> entity = listExerciseRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Lista de exercício não encontrada");
        }

        // Excluir todos os exercícios relacionados á lista
        List<Exercise> exercises = exerciseService.getByListExerciseId(id);
        System.err.println("EXERCÍCIOS RELACIONADOS A LISTA DE ID: " + id);
        for (Exercise exercise : exercises) {
            System.out.println(exercise.toString());
            exerciseService.delete(exercise.getId());
        }

        // Remover associações com turmas
        listExerciseRepository.removeListExerciseAssociationsById(id);

        listExerciseRepository.deleteById(id);
    }

}

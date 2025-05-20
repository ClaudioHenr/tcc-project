package br.com.net.sqlab_backend.domain.list_exercise.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;
import br.com.net.sqlab_backend.domain.list_exercise.services.ListExerciseService;

@RestController
@RequestMapping("api/listexercise")
public class ListExerciseController {

    private ListExerciseService listExerciseService;

    public ListExerciseController(ListExerciseService listExerciseService) {
        this.listExerciseService = listExerciseService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createListExercise(@RequestBody ListExercise request) {
        ListExercise entityCreated = listExerciseService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getListExercise(@PathVariable Long id) {
        ListExercise exercise = listExerciseService.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(exercise);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateListExercise(@PathVariable Long id, @RequestBody ListExercise update) {
        ListExercise exercise = listExerciseService.update(id, update);
        return ResponseEntity.status(HttpStatus.OK).body(exercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteListExercise(@PathVariable Long id) {
        listExerciseService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Lista de exercício excluida com sucesso");
    }

}

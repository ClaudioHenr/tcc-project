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

import br.com.net.sqlab_backend.domain.list_exercise.dto.ResponseCreateListExerciseDTO;
import br.com.net.sqlab_backend.domain.list_exercise.dto.ResponseGetListExerciseDTO;
import br.com.net.sqlab_backend.domain.list_exercise.dto.ResponseUpdateListExerciseDTO;
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
        ResponseCreateListExerciseDTO dto = new ResponseCreateListExerciseDTO(entityCreated.getId(), entityCreated.getTitle(), entityCreated.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getListExercise(@PathVariable Long id) {
        ListExercise entity = listExerciseService.getById(id);
        ResponseGetListExerciseDTO dto = new ResponseGetListExerciseDTO(entity.getId(), entity.getTitle(), entity.getDescription());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateListExercise(@PathVariable Long id, @RequestBody ListExercise update) {
        ListExercise entity = listExerciseService.update(id, update);
        ResponseUpdateListExerciseDTO dto = new ResponseUpdateListExerciseDTO(entity.getId(), entity.getTitle(), entity.getDescription());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteListExercise(@PathVariable Long id) {
        listExerciseService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Lista de exercício excluida com sucesso");
    }

}

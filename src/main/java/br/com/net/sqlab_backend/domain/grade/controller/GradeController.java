package br.com.net.sqlab_backend.domain.grade.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.net.sqlab_backend.domain.grade.dto.RequestCreateGradeDTO;
import br.com.net.sqlab_backend.domain.grade.dto.ResponseCreateGradeDTO;
import br.com.net.sqlab_backend.domain.grade.dto.ResponseGetGradeDTO;
import br.com.net.sqlab_backend.domain.grade.dto.ResponseUpdateGradeDTO;
import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.grade.services.GradeService;
import br.com.net.sqlab_backend.domain.list_exercise.dto.ResponseGetListExerciseDTO;
import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/grades")
public class GradeController {
    
    private GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGrade(@RequestBody RequestCreateGradeDTO create, @RequestParam Long id) {
        Grade gradeCreated = gradeService.save(new Grade(null, create.name(), create.subject(), null, null), id);
        ResponseCreateGradeDTO dto = new ResponseCreateGradeDTO(gradeCreated.getId(), gradeCreated.getName(), gradeCreated.getSubject(), gradeCreated.getCod());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGrade(@PathVariable Long id) {
        Grade grade = gradeService.get(id);
        ResponseGetGradeDTO dto = new ResponseGetGradeDTO(grade.getId(), grade.getName(), grade.getSubject(), grade.getCod());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getGrades(@RequestParam Long id) {
        List<Grade> grades = gradeService.getGradesByProfessorId(id);
        System.out.println(grades);
        List<ResponseGetGradeDTO> dtos = new ArrayList<>();
        for (Grade grade : grades) {
            dtos.add(new ResponseGetGradeDTO(grade.getId(), grade.getName(), grade.getSubject(), grade.getCod()));   
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrade(@PathVariable Long id, @RequestBody Grade update) {
        Grade grade = gradeService.update(id, update);
        ResponseUpdateGradeDTO dto = new ResponseUpdateGradeDTO(grade.getId(), grade.getName(), grade.getSubject(), grade.getCod());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrade(@PathVariable Long id) {
        gradeService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Turma excluida com sucesso");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/listexercises")
    public ResponseEntity<?> getListExercises(@RequestParam Long id) {
        List<ListExercise> listExercises = gradeService.getListExercisesByGradeId(id);
        List<ResponseGetListExerciseDTO> dtos = new ArrayList<>();
        for (ListExercise listExercise : listExercises) {
            dtos.add(new ResponseGetListExerciseDTO(listExercise.getId(), listExercise.getTitle(), listExercise.getDescription()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    
}

package br.com.net.sqlab_backend.domain.grade.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.grade.repository.GradeRepository;
import br.com.net.sqlab_backend.domain.grade.util.RandomCodeGenerator;
import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;

@Service
public class GradeService {
    
    private GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Grade save(Grade grade) {
        String cod;
        do {
            cod = RandomCodeGenerator.generateCode();            
        } while (gradeRepository.existsByCod(cod));
        grade.setCod(cod);
        gradeRepository.save(grade);
        return grade;
    }

    public Grade get(Long id) {
        Optional<Grade> entity = gradeRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Turma não encontrada");
        }
        return entity.get();
    }

    public Grade update(Long id, Grade update) {
        Optional<Grade> entity = gradeRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Turma não encontrada");
        }
        Grade grade = entity.get();
        grade.setName(update.getName());
        grade.setSubject(update.getSubject());

        Grade gradeUptaded = gradeRepository.save(grade);
        return gradeUptaded;
    }

    public void delete(Long id) {
        Optional<Grade> entity = gradeRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Turma não encontrada");
        }
        gradeRepository.deleteById(id);
    }

    public List<ListExercise> getListExercisesByGradeId(Long id) {
        List<ListExercise> listExercises = new ArrayList<>();
        listExercises = gradeRepository.findListExercisesByGradeId(id);
        System.out.println("RESPOSTA");
        System.out.println(listExercises);
        return listExercises;
    }

}

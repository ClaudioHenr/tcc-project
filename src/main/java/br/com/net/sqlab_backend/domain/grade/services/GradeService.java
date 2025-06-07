package br.com.net.sqlab_backend.domain.grade.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.grade.repository.GradeRepository;
import br.com.net.sqlab_backend.domain.grade.util.RandomCodeGenerator;
import br.com.net.sqlab_backend.domain.list_exercise.models.ListExercise;
import br.com.net.sqlab_backend.domain.professor.services.ProfessorService;

@Service
public class GradeService {
    
    private GradeRepository gradeRepository;

    private ProfessorService professorService;

    public GradeService(GradeRepository gradeRepository, ProfessorService professorService) {
        this.gradeRepository = gradeRepository;
        this.professorService = professorService;
    }

    @Transactional
    public Grade save(Grade grade, Long idProfessor) {
        String cod;
        do {
            cod = RandomCodeGenerator.generateCode();            
        } while (gradeRepository.existsByCod(cod));
        
        grade.setCod(cod);
        Grade newGrade = gradeRepository.save(grade);
        
        professorService.addGrades(idProfessor, newGrade);
       
        return grade;
    }

    public Grade get(Long id) {
        Optional<Grade> entity = gradeRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Turma não encontrada");
        }
        return entity.get();
    }

    @Transactional
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
        return listExercises;
    }

    public List<Grade> getGradesByProfessorId(Long id) {
        List<Grade> grades = new ArrayList<>();
        grades = gradeRepository.findGradesByProfessorId(id);
        return grades;
    }

    @Transactional
    public void addListExercises(Long id, ListExercise listExercises) {
        Optional<Grade> optional = gradeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Turma não encontrada");
        }
        Grade grade = optional.get();

        grade.getListExercises().add(listExercises);

        gradeRepository.save(grade);
    }

}

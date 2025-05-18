package br.com.net.sqlab_backend.domain.grade.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.net.sqlab_backend.domain.grade.repository.GradeRepository;
import br.com.net.sqlab_backend.domain.models.ListExercise;

@Service
public class GradeService {
    
    private GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<ListExercise> getListExercisesByGradeId(Long id) {
        List<ListExercise> listExercises = new ArrayList<>();
        listExercises = gradeRepository.findListExercisesByGradeId(id);
        System.out.println("RESPOSTA");
        System.out.println(listExercises);
        return listExercises;
    }

}

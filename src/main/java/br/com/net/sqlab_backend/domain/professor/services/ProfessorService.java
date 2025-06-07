package br.com.net.sqlab_backend.domain.professor.services;

import br.com.net.sqlab_backend.domain.exceptions.custom.EntityNotFoundException;
import br.com.net.sqlab_backend.domain.grade.models.Grade;
import br.com.net.sqlab_backend.domain.professor.models.Professor;
import br.com.net.sqlab_backend.domain.professor.repository.ProfessorGradeRepository;
import br.com.net.sqlab_backend.domain.professor.repository.SelfRegistrationProfessorRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

	private final ProfessorGradeRepository professorGradeRepository;
	private final SelfRegistrationProfessorRepository professorRepository;
	private final PasswordEncoder passwordEncoder;

	public ProfessorService(SelfRegistrationProfessorRepository professorRepository, PasswordEncoder passwordEncoder, ProfessorGradeRepository professorGradeRepository) {
		this.professorRepository = professorRepository;
		this.passwordEncoder = passwordEncoder;
		this.professorGradeRepository = professorGradeRepository;
	}

	public Professor saveProfessor(Professor professor) {
		professor.setPassword(passwordEncoder.encode(professor.getPassword()));
		return professorRepository.save(professor);
	}

	public Optional<Professor> getProfessorById(Long id) {
		return professorRepository.findById(id);
	}

	public Optional<Professor> getProfessorByName(String name) {
		return professorRepository.findByName(name);
	}

	public Optional<Professor> getProfessorByEmail(String email) {
		return professorRepository.findByEmail(email);
	}

	public void deleteProfessor(Long id) {
		professorRepository.deleteById(id);
	}

	public boolean professorExistsByEmail(String email) {
		return professorRepository.existsByEmail(email);
	}

	public void update(Long id, Professor update) {
		Optional<Professor> entity = professorGradeRepository.findById(id);
		entity.get().setName(update.getName());
		entity.get().setEmail(update.getEmail());
		entity.get().setPassword(update.getName());

		professorGradeRepository.save(entity.get());
	}

	public List<Grade> getGrades(Long id) {
		List<Grade> grades = new ArrayList<>();
		grades = professorGradeRepository.findGradesByProfessorId(id);
		return grades;
	}

	@Transactional
    public void addGrades(Long id, Grade grade) {
        Optional<Professor> optional = professorRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Professor não encontrado");
        }
        Professor professor = optional.get();

        professor.getGrades().add(grade);

        professorRepository.save(professor);
    }
}

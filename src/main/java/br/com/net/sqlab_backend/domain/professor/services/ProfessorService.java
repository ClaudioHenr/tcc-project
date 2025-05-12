package br.com.net.sqlab_backend.domain.professor.services;

import br.com.net.sqlab_backend.domain.professor.models.Professor;
import br.com.net.sqlab_backend.domain.professor.repository.SelfRegistrationProfessorRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfessorService {

	private final SelfRegistrationProfessorRepository professorRepository;
	private final PasswordEncoder passwordEncoder;

	public ProfessorService(SelfRegistrationProfessorRepository professorRepository, PasswordEncoder passwordEncoder) {
		this.professorRepository = professorRepository;
		this.passwordEncoder = passwordEncoder;
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
}

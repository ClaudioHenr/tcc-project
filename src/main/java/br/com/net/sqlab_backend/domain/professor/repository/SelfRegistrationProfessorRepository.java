package br.com.net.sqlab_backend.domain.professor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.net.sqlab_backend.domain.professor.models.Professor;

@Repository
public interface SelfRegistrationProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findById(Long id);

    Optional<Professor> findByName(String name);

    Optional<Professor> findByEmail(String email);

    void deleteById(Long id);

    boolean existsByEmail(String email);

}

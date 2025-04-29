package br.com.net.sqlab_backend.domain.student.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.net.sqlab_backend.domain.student.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findById(Long id);

    Optional<Student> findByName(String name);

    Optional<Student> findByEmail(String email);

    void deleteById(Long id);

    boolean existsByEmail(String email);

    boolean existsByRegistrationNumber(String registrationNumber);
}

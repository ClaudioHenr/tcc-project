package br.com.net.sqlab_backend.domain.student.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.net.sqlab_backend.domain.student.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    
}

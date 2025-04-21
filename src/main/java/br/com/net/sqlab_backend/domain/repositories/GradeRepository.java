package br.com.net.sqlab_backend.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.net.sqlab_backend.domain.models.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    
}

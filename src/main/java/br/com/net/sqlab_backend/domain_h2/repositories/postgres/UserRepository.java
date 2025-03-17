package br.com.net.sqlab_backend.domain_h2.repositories.postgres;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.net.sqlab_backend.domain_h2.models.postgres.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    
}

package br.com.net.sqlab_backend.domain_h2.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.net.sqlab_backend.domain_h2.models.auth.Auth;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    
}

package br.com.net.sqlab_backend.domain.shared.models;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserEntity extends UserDetails {
    Long getId();
    String getEmail();
    String getPassword();
}

package br.com.net.sqlab_backend_authentication.repository;

import br.com.net.sqlab_backend.domain.shared.models.UserEntity;
import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByEmail(String email);
}
